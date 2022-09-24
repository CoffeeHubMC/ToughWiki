package me.theseems.toughwiki.api;

import me.theseems.toughwiki.api.view.WikiPageViewManager;

public class ToughWikiAPI {
    private static ToughWikiAPI INSTANCE;

    private final WikiPageViewManager viewManager;
    private final WikiPageRepository pageRepository;

    public ToughWikiAPI(WikiPageViewManager viewManager, WikiPageRepository pageRepository) {
        if (INSTANCE != null) {
            throw new IllegalStateException("ToughtWikiAPI is already initialized");
        }

        this.viewManager = viewManager;
        this.pageRepository = pageRepository;
        INSTANCE = this;
    }

    public WikiPageViewManager getViewManager() {
        return viewManager;
    }

    public WikiPageRepository getPageRepository() {
        return pageRepository;
    }

    public static ToughWikiAPI getInstance() {
        return INSTANCE;
    }
}
