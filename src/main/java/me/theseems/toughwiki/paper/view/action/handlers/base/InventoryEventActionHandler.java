package me.theseems.toughwiki.paper.view.action.handlers.base;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import org.bukkit.event.inventory.InventoryInteractEvent;

public abstract class InventoryEventActionHandler<T extends Action> extends BaseWikiPageActionHandler<T> {
    public InventoryEventActionHandler(Class<T> actionClass) {
        super(actionClass);
    }

    @Override
    protected void handle(T action, ActionSender sender) {
        handle(action, sender, sender.getContainer().getValue("event"));
    }

    @Override
    protected boolean check(T action, ActionSender sender) {
        return sender.getContainer().<InventoryInteractEvent>getValue("event") != null;
    }

    protected abstract void handle(T action, ActionSender sender, InventoryInteractEvent event);
}
