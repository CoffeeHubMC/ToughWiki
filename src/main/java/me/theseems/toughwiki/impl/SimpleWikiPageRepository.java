package me.theseems.toughwiki.impl;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageRepository;
import me.theseems.toughwiki.api.view.WikiPageViewManager;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWikiPageRepository implements WikiPageRepository {
    private final Map<String, WikiPage> wikiPageMap;

    public SimpleWikiPageRepository() {
        wikiPageMap = new ConcurrentHashMap<>();
    }

    @Override
    public void store(WikiPage wikiPage) {
        wikiPageMap.put(wikiPage.getName(), wikiPage);
    }

    @Override
    public void dispose(String name) {
        WikiPage wikiPage = wikiPageMap.get(name);
        WikiPageViewManager viewManager = ToughWikiAPI.getInstance().getViewManager();
        viewManager.getView(wikiPage).ifPresent(viewManager::dispose);
        wikiPageMap.remove(name);
    }

    @Override
    public void clear() {
        wikiPageMap.keySet().forEach(this::dispose);
    }

    @Override
    public Collection<WikiPage> getAllPages() {
        return wikiPageMap.values();
    }

    @Override
    public Optional<WikiPage> getPage(String name) {
        return Optional.ofNullable(wikiPageMap.get(name));
    }
}
