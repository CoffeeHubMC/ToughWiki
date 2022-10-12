package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.BackAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class BackActionHandler extends InventoryEventActionHandler<BackAction> {
    public BackActionHandler() {
        super(BackAction.class);
    }

    @Override
    protected void handle(BackAction action, ActionSender sender, InventoryInteractEvent event) {
        WikiPage page = sender.getView().getPage();
        HumanEntity opener = event.getWhoClicked();

        if (page.getParent().isPresent()) {
            try {
                ToughWikiAPI.getInstance().getViewManager()
                        .getView(page.getParent().get())
                        .orElseThrow(() -> new IllegalStateException(
                                "No parent view found for player '%s' and page '%s'"
                                        .formatted(opener.getName(), page.getName())))
                        .show(opener.getUniqueId());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            opener.closeInventory();
        }
    }
}
