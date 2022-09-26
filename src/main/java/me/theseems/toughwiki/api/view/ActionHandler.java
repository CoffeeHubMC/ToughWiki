package me.theseems.toughwiki.api.view;

public interface ActionHandler {
    boolean supports(Action action, ActionSender sender);

    void proceed(Action action, ActionSender sender);
}
