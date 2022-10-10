package me.theseems.toughwiki.paper.view.action.sender;

import me.theseems.toughwiki.api.view.ActionSender;
import org.bukkit.event.inventory.InventoryInteractEvent;

public interface InteractEventWikiActionSender extends ActionSender {
    InventoryInteractEvent getEvent();
}
