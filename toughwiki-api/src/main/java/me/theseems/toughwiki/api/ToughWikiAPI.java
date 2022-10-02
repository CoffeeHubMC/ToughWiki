package me.theseems.toughwiki.api;

import me.theseems.toughwiki.api.view.ActionEmitter;
import me.theseems.toughwiki.api.view.WikiPageViewManager;

public class ToughWikiAPI {
    private static ToughWikiAPI INSTANCE;

    private final WikiPageViewManager viewManager;
    private final WikiPageRepository pageRepository;
    private final ActionEmitter actionEmitter;

    public ToughWikiAPI(WikiPageViewManager viewManager, WikiPageRepository pageRepository, ActionEmitter actionEmitter) {
        if (INSTANCE != null) {
            throw new IllegalStateException("ToughtWikiAPI is already initialized");
        }

        this.viewManager = viewManager;
        this.pageRepository = pageRepository;
        this.actionEmitter = actionEmitter;
        INSTANCE = this;
    }

    public static ToughWikiAPI getInstance() {
        return INSTANCE;
    }

    public WikiPageViewManager getViewManager() {
        return viewManager;
    }

    public WikiPageRepository getPageRepository() {
        return pageRepository;
    }

    public ActionEmitter getActionEmitter() {
        return actionEmitter;
    }
}
