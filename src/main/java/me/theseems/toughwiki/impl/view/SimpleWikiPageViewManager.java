package me.theseems.toughwiki.impl.view;

import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.api.view.WikiPageViewManager;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWikiPageViewManager implements WikiPageViewManager {
    private final Map<WikiPage, WikiPageView> viewMap;

    public SimpleWikiPageViewManager() {
        viewMap = new ConcurrentHashMap<>();
    }

    @Override
    public void store(WikiPageView wikiPageView) {
        viewMap.put(wikiPageView.getPage(), wikiPageView);
    }

    @Override
    public void dispose(WikiPageView wikiPageView) {
        viewMap.remove(wikiPageView.getPage(), wikiPageView);
    }

    @Override
    public Collection<WikiPageView> getAllViews() {
        return viewMap.values();
    }

    @Override
    public Optional<WikiPageView> getView(WikiPage wikiPage) {
        return Optional.ofNullable(viewMap.get(wikiPage));
    }
}
