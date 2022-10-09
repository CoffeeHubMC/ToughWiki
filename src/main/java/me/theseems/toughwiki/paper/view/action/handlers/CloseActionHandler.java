package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.CloseAction;

public class CloseActionHandler extends IFWikiPageActionHandler<CloseAction> {
    public CloseActionHandler() {
        super(CloseAction.class);
    }

    @Override
    public boolean supports(CloseAction action, IFWikiActionSender sender) {
        return true;
    }

    @Override
    protected void proceed(CloseAction action, IFWikiActionSender sender) {
        sender.getEvent().getWhoClicked().closeInventory();
    }
}
