package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionHandler;
import me.theseems.toughwiki.api.view.ActionSender;

@SuppressWarnings("unchecked")
public abstract class IFWikiPageActionHandler<T extends Action> implements ActionHandler {
    private final Class<T> tClass;

    public IFWikiPageActionHandler(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public boolean supports(Action action, ActionSender sender) {
        return tClass.isAssignableFrom(action.getClass())
                && sender instanceof IFWikiActionSender
                && supports((T) action, (IFWikiActionSender) sender);
    }

    @Override
    public void proceed(Action action, ActionSender sender) {
        proceed((T) action, (IFWikiActionSender) sender);
    }

    protected abstract void proceed(T action, IFWikiActionSender sender);

    protected boolean supports(T action, IFWikiActionSender sender) {
        return true;
    }
}
