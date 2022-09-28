package me.theseems.toughwiki.paper.view;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.CommandActionHandler;
import me.theseems.toughwiki.paper.view.action.handlers.GotoActionHandler;
import me.theseems.toughwiki.paper.view.action.handlers.SwitchActionHandler;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        PlayerGUIContext context = playerGUIMap.computeIfAbsent(player, uuid -> makeContext(onlinePlayer, null));
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
            newContext.context = new ObjectNode(new ObjectMapper().getNodeFactory());
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
            int slot = getSlot(content);
            if (slot == -1) {
                continue;
            }

            GuiItem item = makeItem(player, chestGui, content, context);

            pane.removeItem(slot % 9, slot / 9);
            pane.addItem(item, slot % 9, slot / 9);
        }

        chestGui.addPane(pane);
        chestGui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        chestGui.setOnGlobalDrag(inventoryDragEvent -> inventoryDragEvent.setCancelled(true));
        chestGui.update();

        return chestGui;
    }

    public GuiItem makeItem(Player player, ChestGui chestGui, WikiPageItemConfig content, ObjectNode context) {
        ItemStack stack = ToughWiki.getItemFactory().produce(player, content);
        int slot = getSlot(content);
        if (context == null) {
            context = playerGUIMap.get(player.getUniqueId()).context;
        }

        Action currentAction = getAction(content);
        if (GotoActionHandler.getGoto(content) != null) {
            currentAction = Action.GOTO;
        }
        if (CommandActionHandler.getCommand(content) != null) {
            currentAction = Action.COMMAND;
        }
        if (SwitchActionHandler.getSwitchTo(content) != null) {
            currentAction = Action.SWITCH_ITEM;
        }

        Action finalCurrentAction = currentAction;
        if (slot != -1) {
            int currentLinesOffset = Optional.ofNullable(context.get("scroll-offset-item-" + slot))
                    .filter(JsonNode::isInt)
                    .map(JsonNode::asInt)
                    .orElse(0);

            ItemMeta meta = stack.getItemMeta();
            if (meta.lore() != null && meta.lore().size() > getMaxLines()) {
                Stream<Component> componentStream = Objects.requireNonNull(meta.lore()).stream()
                        .skip(currentLinesOffset)
                        .limit(getMaxLines());
                if (currentAction == Action.SCROLL_ITEM && getSeeMore() != null) {
                    componentStream = Stream.concat(componentStream, getSeeMore().stream());
                }
                meta.lore(componentStream.collect(Collectors.toList()));
            }

            stack.setItemMeta(meta);
        }

        GuiItem guiItem = new GuiItem(stack);
        guiItem.setAction(inventoryClickEvent -> {
            try {
                ToughWikiAPI.getInstance().getActionEmitter().emit(
                        finalCurrentAction,
                        new IFWikiActionSender(this,
                                content,
                                chestGui,
                                guiItem,
                                getSlot(content),
                                inventoryClickEvent));
            } finally {
                inventoryClickEvent.setCancelled(true);
            }
        });

        return guiItem;
    }

    public GuiItem makeItem(Player player, ChestGui chestGui, WikiPageItemConfig content) {
        return makeItem(player, chestGui, content, null);
    }

    private int getSlot(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("slot")) {
            JsonNode slot = config.getModifiers().get("slot");
            if (slot.isNumber()) {
                return slot.intValue();
            }
        }
        return -1;
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

    private Action getAction(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("action")) {
            JsonNode action = config.getModifiers().get("action");
            if (!action.isTextual()) {
                ToughWiki.getPluginLogger()
                        .warning("Could not find an action for the item config (incorrect value): " + config + " (" + action + ")");
            } else {
                try {
                    return Action.valueOf(action.asText());
                } catch (IllegalArgumentException e) {
                    ToughWiki.getPluginLogger()
                            .warning("Invalid action specified for config " + config + ": '" + action + "'");
                }
            }
        }

        return null;
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
                    '}';
        }
    }
}
