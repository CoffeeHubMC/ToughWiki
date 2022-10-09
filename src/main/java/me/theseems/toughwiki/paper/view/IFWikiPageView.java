package me.theseems.toughwiki.paper.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.SoundAction;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class IFWikiPageView implements WikiPageView {
    public static final Duration PER_PLAYER_CACHE_TTL = Duration.ofSeconds(5L);
    private final String wikiPageName;
    private final Map<UUID, PlayerGUIContext> playerGUIMap;
    private final ObjectNode defaultContext;

    public IFWikiPageView(String wikiPageName, ObjectNode defaultContext) {
        this.wikiPageName = wikiPageName;
        this.playerGUIMap = new ConcurrentHashMap<>();
        this.defaultContext = defaultContext == null ? new ObjectNode(new ObjectMapper().getNodeFactory()) : defaultContext;
    }

    @Override
    public WikiPage getPage() {
        return ToughWikiAPI.getInstance()
                .getPageRepository()
                .getPage(wikiPageName)
                .orElseThrow(() -> new IllegalStateException(
                        "Wiki page '" + wikiPageName + "' was not found but view is not disposed"));
    }

    @Override
    public String getName() {
        return wikiPageName;
    }

    @Override
    public void show(UUID player) {
        Player onlinePlayer = Bukkit.getPlayer(player);
        if (onlinePlayer == null) {
            dispose(player);
            return;
        }

        PlayerGUIContext context = playerGUIMap
                .computeIfAbsent(player, uuid -> makeContext(onlinePlayer, null));

        if (isInvalidationAvailable()
                && Duration.between(context.lastUpdate, ZonedDateTime.now()).compareTo(PER_PLAYER_CACHE_TTL) > 0) {
            dispose(player);
            context = makeContext(onlinePlayer, context);
        }

        for (HumanEntity viewer : context.chestGui.getViewers()) {
            viewer.closeInventory();
        }

        context.chestGui.show(onlinePlayer);
    }

    @Override
    public void dispose(UUID player) {
        if (!playerGUIMap.containsKey(player)) {
            return;
        }

        playerGUIMap.remove(player);
    }

    @Override
    public void dispose() {
        playerGUIMap.forEach((uuid, playerGUIContext) -> {
            dispose(uuid);
            playerGUIMap.remove(uuid);
        });
    }

    @Override
    public ObjectNode getContext(UUID player) {
        if (!playerGUIMap.containsKey(player)) {
            return defaultContext;
        }

        return playerGUIMap.get(player).context;
    }

    @Override
    public ObjectNode getGlobalContext() {
        return defaultContext;
    }

    private PlayerGUIContext makeContext(Player player, PlayerGUIContext previous) {
        PlayerGUIContext newContext = new PlayerGUIContext();
        newContext.lastUpdate = ZonedDateTime.now();
        if (previous != null) {
            newContext.context = previous.context;
        } else {
            newContext.context = new ObjectNode(new ObjectMapper(new YAMLFactory()).getNodeFactory());
        }
        newContext.chestGui = makeChestGUI(player, newContext.context);
        return newContext;
    }

    private ChestGui makeChestGUI(Player player, ObjectNode context) {
        WikiPage wikiPage = getPage();

        int size = wikiPage.getInfo().getSize();

        TextHolder textHolder = ComponentHolder.of(TextUtils.parse(wikiPage.getInfo().getTitle()));
        ChestGui chestGui = new ChestGui(size, textHolder);

        StaticPane pane = new StaticPane(0, 0, 9, size);

        for (WikiPageItemConfig content : wikiPage.getInfo().getItems()) {
            for (Integer slot : getSlots(content)) {
                GuiItem item = makeItem(player, chestGui, content, context, slot);

                pane.removeItem(slot % 9, slot / 9);
                pane.addItem(item, slot % 9, slot / 9);
            }
        }

        chestGui.addPane(pane);
        chestGui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        chestGui.setOnGlobalDrag(inventoryDragEvent -> inventoryDragEvent.setCancelled(true));
        chestGui.update();

        return chestGui;
    }

    public GuiItem makeItem(Player player,
                            ChestGui chestGui,
                            WikiPageItemConfig content,
                            ObjectNode context,
                            Integer slot) {
        if (slot == null) {
            slot = -1;
        }

        ItemStack stack = ToughWiki.getItemFactory().produce(player, content);

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Map<TriggerType, Action> actionMap = new HashMap<>();

        if (content.getModifiers().containsKey("leftClickAction")) {
            TriggerType type = TriggerType.LEFT_MOUSE_BUTTON;
            Action action = ToughWiki.getActionFactory()
                    .produce(type, (ObjectNode) content.getModifiers().get("leftClickAction"));

            if (action != null) {
                actionMap.put(type, action);
            }
        }
        if (content.getModifiers().containsKey("rightClickAction")) {
            TriggerType type = TriggerType.RIGHT_MOUSE_BUTTON;
            Action action = ToughWiki.getActionFactory()
                    .produce(type, (ObjectNode) content.getModifiers().get("rightClickAction"));

            if (action != null) {
                actionMap.put(type, action);
            }
        }
        if (actionMap.isEmpty()) {
            for (TriggerType value : TriggerType.values()) {
                Action produced = ToughWiki.getActionFactory().produce(value, mapper.valueToTree(content));
                if (produced == null) {
                    continue;
                }

                actionMap.put(value, produced);
            }
        }

        GuiItem guiItem = new GuiItem(stack);
        int finalSlot = slot;
        guiItem.setAction(inventoryClickEvent -> {
            try {
                TriggerType type;
                switch (inventoryClickEvent.getClick()) {
                    case LEFT -> type = TriggerType.LEFT_MOUSE_BUTTON;
                    case RIGHT -> type = TriggerType.RIGHT_MOUSE_BUTTON;
                    default -> {
                        inventoryClickEvent.setCancelled(true);
                        return;
                    }
                }

                if (!actionMap.containsKey(type)) {
                    inventoryClickEvent.setCancelled(true);
                    return;
                }

                Action action = actionMap.get(type);
                if (!(action instanceof SoundAction)) {
                    inventoryClickEvent.getWhoClicked().playSound(
                            Sound.sound(
                                    Key.key("minecraft:ui.button.click"),
                                    Sound.Source.AMBIENT,
                                    1f, 1f));
                }

                ToughWikiAPI.getInstance().getActionEmitter().emit(
                        action,
                        new IFWikiActionSender(this,
                                content,
                                chestGui,
                                guiItem,
                                finalSlot,
                                inventoryClickEvent));
            } finally {
                inventoryClickEvent.setCancelled(true);
            }
        });

        return guiItem;
    }

    public GuiItem makeItem(Player player, ChestGui chestGui, WikiPageItemConfig content, Integer slot) {
        return makeItem(player, chestGui, content, null, slot);
    }

    private List<Integer> getSlots(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("slot")) {
            JsonNode slot = config.getModifiers().get("slot");
            if (slot.isNumber()) {
                return Collections.singletonList(slot.asInt());
            } else if (slot.isArray()) {
                List<Integer> slots = new LinkedList<>();
                slot.elements().forEachRemaining(jsonNode -> {
                    if (jsonNode.isNumber()) {
                        slots.add(jsonNode.asInt());
                    }
                });

                return slots;
            }
        }
        return Collections.emptyList();
    }

    public WikiPageItemConfig getRef(String reference) {
        for (WikiPageItemConfig item : getPage().getInfo().getItems()) {
            Map<String, JsonNode> modifiers = item.getModifiers();
            if (modifiers.containsKey("ref")
                    && modifiers.get("ref").isTextual()
                    && modifiers.get("ref").asText().equals(reference)) {
                return item;
            }
        }

        return null;
    }

    public int getMaxLines() {
        return Optional.ofNullable(defaultContext.get("maxLines"))
                .filter(JsonNode::isInt)
                .map(JsonNode::asInt)
                .orElse(999);
    }

    public boolean isInvalidationAvailable() {
        return Optional.ofNullable(defaultContext.get("invalidation"))
                .filter(JsonNode::isBoolean)
                .map(JsonNode::asBoolean).orElse(false);
    }

    public List<Component> getSeeMore() {
        JsonNode array = Optional.ofNullable(defaultContext.get("seeMoreLines"))
                .filter(JsonNode::isArray)
                .orElse(null);

        if (array == null) {
            return null;
        }

        List<Component> seeMoreList = new ArrayList<>();
        array.forEach(jsonElement -> {
            if (!jsonElement.isTextual()) {
                throw new IllegalStateException("seeMoreLines must contain only strings");
            }

            seeMoreList.add(TextUtils.parse(jsonElement.asText()));
        });

        return seeMoreList;
    }

    public Map<UUID, PlayerGUIContext> getPlayerGUIMap() {
        return playerGUIMap;
    }

    private static class PlayerGUIContext {
        private ZonedDateTime lastUpdate;
        private ChestGui chestGui;
        private ObjectNode context;

        @Override
        public String toString() {
            return "PlayerGUIContext{" +
                    "lastUpdate=" + lastUpdate +
                    ", chestGui=" + chestGui +
                    ", context=" + context +
                    '}';
        }
    }
}
