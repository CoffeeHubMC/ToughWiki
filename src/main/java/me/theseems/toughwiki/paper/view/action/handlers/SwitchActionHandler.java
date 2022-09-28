package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import org.bukkit.entity.Player;

public class SwitchActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.SWITCH_ITEM;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        if (!(sender.getEvent().getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = ((Player) sender.getEvent().getWhoClicked()).getPlayer();
        WikiPageItemConfig ref = sender.getView().getRef(getSwitchTo(sender.getItemConfig()));
        if (ref == null) {
            throw new IllegalStateException("Could not find item by ref: '" + getSwitchTo(sender.getItemConfig()) + "'");
        }

        GuiItem item = sender.getView().makeItem(player, sender.getChestGui(), ref);
        sender.replaceGUIItem(sender.getSlot(), item);
    }

    public static String getSwitchTo(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("switchTo")) {
            JsonNode action = config.getModifiers().get("switchTo");
            if (!action.isTextual()) {
                ToughWiki.getPluginLogger()
                        .warning("Could not parse a ref to item to switch: " + config + " (" + action + ")");
            } else {
                return action.asText();
            }
        }

        return null;
    }
}
