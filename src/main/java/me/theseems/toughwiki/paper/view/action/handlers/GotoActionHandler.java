package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import org.bukkit.entity.HumanEntity;

public class GotoActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.GOTO;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        HumanEntity humanEntity = sender.getEvent().getWhoClicked();

        String gotoName = getGoto(sender.getItemConfig());
        WikiPage target = ToughWikiAPI.getInstance()
                .getPageRepository()
                .getPage(gotoName)
                .orElseThrow(() -> new IllegalStateException("Target page '" + gotoName + "' is not found"));

        ToughWikiAPI.getInstance()
                .getViewManager()
                .getView(target)
                .orElseThrow(() -> new IllegalStateException("No target (goto) view found for page '" + gotoName + "'"))
                .show(humanEntity.getUniqueId());
    }

    private String getGoto(WikiPageItemConfig config) {
        if (config.getModifiers() != null && config.getModifiers().containsKey("goto")) {
            JsonNode action = config.getModifiers().get("goto");
            if (!action.isTextual()) {
                ToughWiki.getPluginLogger()
                        .warning("Could not find a target for goto: " + config + " (" + action + ")");
            } else {
                return action.asText();
            }
        }

        return null;
    }
}
