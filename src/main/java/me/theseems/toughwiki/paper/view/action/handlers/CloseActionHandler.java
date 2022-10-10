package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.InteractEventWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.CloseAction;

public class CloseActionHandler extends BaseWikiPageActionHandler<InteractEventWikiActionSender, CloseAction> {
    public CloseActionHandler() {
        super(InteractEventWikiActionSender.class, CloseAction.class);
    }

    @Override
    protected void handle(CloseAction action, InteractEventWikiActionSender sender) {
        sender.getEvent().getWhoClicked().closeInventory();
    }
}
