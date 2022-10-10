package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.InteractEventWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.CommandAction;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Objects;

public class CommandActionHandler extends BaseWikiPageActionHandler<InteractEventWikiActionSender, CommandAction> {
    public CommandActionHandler() {
        super(InteractEventWikiActionSender.class, CommandAction.class);
    }

    @Override
    protected void handle(CommandAction action, InteractEventWikiActionSender sender) {
        HumanEntity humanEntity = sender.getEvent().getWhoClicked();
        humanEntity.closeInventory();

        if (humanEntity instanceof Player) {
            ((Player) humanEntity).performCommand(Objects.requireNonNull(action.getCommandName()));
        }
    }
}
