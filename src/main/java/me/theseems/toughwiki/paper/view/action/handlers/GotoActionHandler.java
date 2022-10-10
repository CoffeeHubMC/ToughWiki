package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.InteractEventWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.GotoAction;
import org.bukkit.entity.HumanEntity;

public class GotoActionHandler extends BaseWikiPageActionHandler<InteractEventWikiActionSender, GotoAction> {

    public GotoActionHandler() {
        super(InteractEventWikiActionSender.class, GotoAction.class);
    }

    @Override
    protected void handle(GotoAction action, InteractEventWikiActionSender sender) {
        HumanEntity humanEntity = sender.getEvent().getWhoClicked();

        String gotoName = action.getGotoName();
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
}
