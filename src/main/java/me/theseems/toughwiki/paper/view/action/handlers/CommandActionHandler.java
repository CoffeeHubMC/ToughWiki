package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.CommandAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandActionHandler extends IFWikiPageActionHandler<CommandAction> {
    public CommandActionHandler() {
        super(CommandAction.class);
    }

    @Override
    protected void proceed(CommandAction action, IFWikiActionSender sender) {
        HumanEntity humanEntity = sender.getEvent().getWhoClicked();
        humanEntity.closeInventory();

        if (humanEntity instanceof Player) {
            ((Player) humanEntity).performCommand(Objects.requireNonNull(action.getCommandName()));
        }
    }
}
