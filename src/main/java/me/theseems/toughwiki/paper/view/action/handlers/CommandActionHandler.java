package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.CommandAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

import java.util.Objects;

public class CommandActionHandler extends InventoryEventActionHandler<CommandAction> {
    public CommandActionHandler() {
        super(CommandAction.class);
    }

    @Override
    protected void handle(CommandAction action, ActionSender sender, InventoryInteractEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        humanEntity.closeInventory();

        if (humanEntity instanceof Player) {
            ((Player) humanEntity).performCommand(Objects.requireNonNull(action.getCommandName()));
        }
    }
}
