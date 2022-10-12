package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.CloseAction;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class CloseActionHandler extends InventoryEventActionHandler<CloseAction> {
    public CloseActionHandler() {
        super(CloseAction.class);
    }

    @Override
    protected void handle(CloseAction action, ActionSender sender, InventoryInteractEvent event) {
        event.getWhoClicked().closeInventory();
    }
}
