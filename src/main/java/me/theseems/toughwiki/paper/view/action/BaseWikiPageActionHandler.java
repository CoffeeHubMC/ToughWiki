package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionHandler;
import me.theseems.toughwiki.api.view.ActionSender;

@SuppressWarnings("unchecked")
public abstract class BaseWikiPageActionHandler<A extends Action> implements ActionHandler {
    private final Class<A> actionClass;

    public BaseWikiPageActionHandler(Class<A> actionClass) {
        this.actionClass = actionClass;
    }

    @Override
    public boolean supports(Action action, ActionSender sender) {
        return actionClass.isAssignableFrom(action.getClass())
                && check((A) action, sender);
    }

    @Override
    public void proceed(Action action, ActionSender sender) {
        handle((A) action, sender);
    }

    protected abstract void handle(A action, ActionSender sender);

    protected boolean check(A action, ActionSender sender) {
        return true;
    }
}
