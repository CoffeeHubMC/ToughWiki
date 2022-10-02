package me.theseems.toughwiki.impl;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.ActionEmitter;
import me.theseems.toughwiki.api.view.ActionHandler;
import me.theseems.toughwiki.api.view.ActionSender;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleActionEmitter implements ActionEmitter {
    private final List<ActionHandler> handlerList;

    public SimpleActionEmitter() {
        handlerList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void emit(Action action, ActionSender sender) {
        for (ActionHandler actionHandler : handlerList) {
            if (actionHandler.supports(action, sender)) {
                actionHandler.proceed(action, sender);
            }
        }
    }

    @Override
    public void register(ActionHandler handler) {
        handlerList.add(handler);
    }

    @Override
    public void unregister(ActionHandler handler) {
        handlerList.remove(handler);
    }
}
