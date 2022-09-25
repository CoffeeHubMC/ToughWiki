package me.theseems.toughwiki.paper.view;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.adventuresupport.TextHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IFWikiPageView implements WikiPageView {
    public static final Duration PER_PLAYER_CACHE_TTL = Duration.ofSeconds(120L);

    private static class PlayerGUIContext {
        private ZonedDateTime lastUpdate;
        private ChestGui chestGui;

        @Override
        public String toString() {
            return "PlayerGUIContext{" +
                    "lastUpdate=" + lastUpdate +
                    ", chestGui=" + chestGui +
                    '}';
        }
    }

    private final String wikiPageName;
    private final Map<UUID, PlayerGUIContext> playerGUIMap;

    public IFWikiPageView(String wikiPageName) {
        this.wikiPageName = wikiPageName;
        this.playerGUIMap = new ConcurrentHashMap<>();
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

        PlayerGUIContext context = playerGUIMap.computeIfAbsent(player, uuid -> makeContext(onlinePlayer));
        if (Duration.between(context.lastUpdate, ZonedDateTime.now()).compareTo(PER_PLAYER_CACHE_TTL) > 0) {
            dispose(player);
            context = makeContext(onlinePlayer);
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

    private PlayerGUIContext makeContext(Player player) {
        PlayerGUIContext newContext = new PlayerGUIContext();
        newContext.lastUpdate = ZonedDateTime.now();
        newContext.chestGui = makeChestGUI(player);
        return newContext;
    }

    private ChestGui makeChestGUI(Player player) {
        WikiPage wikiPage = getPage();

        int size = wikiPage.getInfo().getSize();

        TextHolder textHolder = ComponentHolder.of(TextUtils.parse(wikiPage.getInfo().getTitle()));
        ChestGui chestGui = new ChestGui(size, textHolder);

        StaticPane pane = new StaticPane(0, 0, 9, size);
        for (WikiPageItemConfig content : wikiPage.getInfo().getItems()) {
            ItemStack stack = ToughWiki.getItemFactory().produce(player, content);
            Runnable action = () -> {
            };

            Action currentAction = getAction(content);
            if (getGoto(content) != null) {
                currentAction = Action.GOTO;
            }
            if (getCommand(content) != null) {
                currentAction = Action.COMMAND;
            }

            if (currentAction != null) {
                switch (currentAction) {
                    case BACK -> action = () -> {
                        if (getPage().getParent().isPresent()) {
                            try {
                                ToughWikiAPI.getInstance().getViewManager()
                                        .getView(getPage().getParent().get())
                                        .orElseThrow(() -> new IllegalStateException(
                                                "No parent view found for player '%s' and page '%s'"
                                                        .formatted(player.getName(), getPage().getName())))
                                        .show(player.getUniqueId());
                            } catch (Throwable e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            player.closeInventory();
                        }
                    };

                    case GOTO -> action = () -> {
                        WikiPage target = ToughWikiAPI.getInstance()
                                .getPageRepository()
                                .getPage(getGoto(content))
                                .orElseThrow(() -> new IllegalStateException("Target page '" + getGoto(content) + "' is not found"));

                        ToughWikiAPI.getInstance()
                                .getViewManager()
                                .getView(target)
                                .orElseThrow(() -> new IllegalStateException("No target (goto) view found for page '" + getGoto(content) + "'"))
                                .show(player.getUniqueId());
                    };

                    case COMMAND -> action = () -> {
                        player.closeInventory();
                        player.performCommand(Objects.requireNonNull(getCommand(content)));
                    };

                    case CLOSE -> action = player::closeInventory;

                    default -> throw new IllegalStateException("Action is unsupported '" + action + "'");
                }
            }


            Runnable finalAction = action;
            GuiItem guiItem = new GuiItem(stack, inventoryClickEvent -> {
                try {
                    finalAction.run();
                } finally {
                    inventoryClickEvent.setCancelled(true);
                }
            });

            int slot = getSlot(content);
            pane.removeItem(slot % 9, slot / 9);
            pane.addItem(guiItem, slot % 9, slot / 9);
        }

        chestGui.addPane(pane);
        chestGui.setOnGlobalClick(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        chestGui.setOnGlobalDrag(inventoryDragEvent -> inventoryDragEvent.setCancelled(true));
        chestGui.update();

        return chestGui;
    }

    private int getSlot(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("slot")) {
            Object slot = config.getModifiers().get("slot");
            if (!(slot instanceof Number)) {
                ToughWiki.getPluginLogger()
                        .warning("Could not find a slot for the item config (incorrect value): " + config);
            } else {
                return ((Number) slot).intValue();
            }
        }

        ToughWiki.getPluginLogger().warning("Could not find a slot for the item config: " + config);
        return -1;
    }

    private Action getAction(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("action")) {
            Object action = config.getModifiers().get("action");
            if (!(action instanceof String)) {
                ToughWiki.getPluginLogger()
                        .warning("Could not find an action for the item config (incorrect value): " + config);
            } else {
                try {
                    return Action.valueOf((String) action);
                } catch (IllegalArgumentException e) {
                    ToughWiki.getPluginLogger()
                            .warning("Invalid action specified for config " + config + ": '" + action + "'");
                }
            }
        }

        return null;
    }

    private String getGoto(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("goto")) {
            Object action = config.getModifiers().get("goto");
            if (!(action instanceof String)) {
                ToughWiki.getPluginLogger()
                        .warning("Could not find a target for goto: " + config);
            } else {
                return (String) action;
            }
        }

        return null;
    }

    private String getCommand(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("command")) {
            Object action = config.getModifiers().get("command");
            if (!(action instanceof String)) {
                ToughWiki.getPluginLogger()
                        .warning("Could not parse a command: " + config);
            } else {
                return (String) action;
            }
        }

        return null;
    }
}
