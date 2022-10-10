package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionHandler;
import me.theseems.toughwiki.api.view.ActionSender;

@SuppressWarnings("unchecked")
public abstract class BaseWikiPageActionHandler<S extends ActionSender, A extends Action> implements ActionHandler {
    private final Class<S> senderClass;
    private final Class<A> actionClass;

    public BaseWikiPageActionHandler(Class<S> senderClass, Class<A> actionClass) {
        this.senderClass = senderClass;
        this.actionClass = actionClass;
    }

    @Override
    public boolean supports(Action action, ActionSender sender) {
        return senderClass.isAssignableFrom(sender.getClass())
                && actionClass.isAssignableFrom(action.getClass())
                && check((A) action, (S) sender);
    }

    @Override
    public void proceed(Action action, ActionSender sender) {
        handle((A) action, (S) sender);
    }

    protected abstract void handle(A action, S sender);

    protected boolean check(A action, S sender) {
        return true;
    }
}
