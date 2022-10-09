package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.GotoAction;
import org.bukkit.entity.HumanEntity;

public class GotoActionHandler extends IFWikiPageActionHandler<GotoAction> {

    public GotoActionHandler() {
        super(GotoAction.class);
    }

    @Override
    protected void proceed(GotoAction action, IFWikiActionSender sender) {
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
