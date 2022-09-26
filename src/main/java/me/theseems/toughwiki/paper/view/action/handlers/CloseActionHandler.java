package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;

public class CloseActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.CLOSE;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        sender.getEvent().getWhoClicked().closeInventory();
    }
}
