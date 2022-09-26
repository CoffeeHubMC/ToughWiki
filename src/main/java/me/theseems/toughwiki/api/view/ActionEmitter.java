package me.theseems.toughwiki.api.view;

public interface ActionEmitter {
    void emit(Action action, ActionSender sender);

    void register(ActionHandler handler);

    void unregister(ActionHandler handler);
}
