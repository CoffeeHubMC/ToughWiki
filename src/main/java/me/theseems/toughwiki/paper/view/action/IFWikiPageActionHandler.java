package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionHandler;
import me.theseems.toughwiki.api.view.ActionSender;

public abstract class IFWikiPageActionHandler implements ActionHandler {
    @Override
    public boolean supports(Action action, ActionSender sender) {
        return sender instanceof IFWikiActionSender && supports(action, (IFWikiActionSender) sender);
    }

    @Override
    public void proceed(Action action, ActionSender sender) {
        proceed(action, (IFWikiActionSender) sender);
    }

    protected abstract void proceed(Action action, IFWikiActionSender sender);

    protected abstract boolean supports(Action action, IFWikiActionSender sender);
}
