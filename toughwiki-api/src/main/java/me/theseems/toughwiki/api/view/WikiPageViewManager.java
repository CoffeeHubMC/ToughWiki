package me.theseems.toughwiki.api.view;

import me.theseems.toughwiki.api.WikiPage;

import java.util.Collection;
import java.util.Optional;

public interface WikiPageViewManager {
    void store(WikiPageView wikiPageView);

    void dispose(String page);

    Collection<WikiPageView> getAllViews();

    Optional<WikiPageView> getView(WikiPage wikiPage);

    Optional<WikiPageViewFactory> getFactory(String type);

    void storeFactory(WikiPageViewFactory factory);

    void removeFactory(String type);
}
