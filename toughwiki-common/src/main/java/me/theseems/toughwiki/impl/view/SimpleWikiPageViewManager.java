package me.theseems.toughwiki.impl.view;

import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.api.view.WikiPageViewManager;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWikiPageViewManager implements WikiPageViewManager {
    private final Map<String, WikiPageView> viewMap;

    public SimpleWikiPageViewManager() {
        viewMap = new ConcurrentHashMap<>();
    }

    @Override
    public void store(WikiPageView wikiPageView) {
        if (viewMap.containsKey(wikiPageView.getPage().getName())) {
            throw new IllegalStateException("View for '" + wikiPageView.getPage().getName() + "' is already stored");
        }

        viewMap.put(wikiPageView.getPage().getName(), wikiPageView);
    }

    @Override
    public void dispose(String page) {
        if (!viewMap.containsKey(page)) {
            return;
        }

        WikiPageView view = viewMap.get(page);
        view.dispose();
        viewMap.remove(page);
    }

    @Override
    public Collection<WikiPageView> getAllViews() {
        return viewMap.values();
    }

    @Override
    public Optional<WikiPageView> getView(WikiPage wikiPage) {
        return Optional.ofNullable(viewMap.get(wikiPage.getName()));
    }
}
