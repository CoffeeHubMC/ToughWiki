package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.COMMAND;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        HumanEntity humanEntity = sender.getEvent().getWhoClicked();
        humanEntity.closeInventory();

        if (humanEntity instanceof Player) {
            ((Player) humanEntity).performCommand(
                    Objects.requireNonNull(getCommand(sender.getItemConfig())));
        }
    }

    public static String getCommand(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("command")) {
            JsonNode action = config.getModifiers().get("command");
            if (!action.isTextual()) {
                ToughWiki.getPluginLogger()
                        .warning("Could not parse a command: " + config + " (" + action + ")");
            } else {
                return action.asText();
            }
        }

        return null;
    }
}
