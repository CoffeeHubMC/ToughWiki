package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.GotoAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class GotoActionHandler extends InventoryEventActionHandler<GotoAction> {

    public GotoActionHandler() {
        super(GotoAction.class);
    }

    @Override
    protected void handle(GotoAction action, ActionSender sender, InventoryInteractEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();

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
