package me.theseems.toughwiki.paper.view.action.handlers;

import me.clip.placeholderapi.PlaceholderAPI;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.ConsoleCommandAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class ConsoleCommandActionHandler extends InventoryEventActionHandler<ConsoleCommandAction> {
    public ConsoleCommandActionHandler() {
        super(ConsoleCommandAction.class);
    }

    @Override
    protected void handle(ConsoleCommandAction action, ActionSender sender, InventoryInteractEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();
        if (!(humanEntity instanceof Player)) {
            return;
        }

        String commandName = action.getCommandName();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            commandName = PlaceholderAPI.setPlaceholders((Player) humanEntity, commandName);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandName);
    }
}
