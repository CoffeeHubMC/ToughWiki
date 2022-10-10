package me.theseems.toughwiki.impl.view;

import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.api.view.WikiPageViewFactory;
import me.theseems.toughwiki.api.view.WikiPageViewManager;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleWikiPageViewManager implements WikiPageViewManager {
    public static final String VIEW_FACTORY_SEPARATOR = "::";
    private final Map<String, WikiPageView> viewMap;
    private final Map<String, WikiPageViewFactory> factoryMap;

    public SimpleWikiPageViewManager() {
        viewMap = new ConcurrentHashMap<>();
        factoryMap = new ConcurrentHashMap<>();
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
        String[] splintered = wikiPage.getName().split(VIEW_FACTORY_SEPARATOR);
        if (splintered.length == 1) {
            return Optional.ofNullable(viewMap.get(wikiPage.getName()));
        } else {
            return Optional.ofNullable(factoryMap.get(splintered[0]))
                    .flatMap(factory -> factory.produce(wikiPage, splintered[1]));
        }
    }

    @Override
    public Optional<WikiPageViewFactory> getFactory(String type) {
        return Optional.of(factoryMap.get(type));
    }

    @Override
    public void storeFactory(WikiPageViewFactory factory) {
        if (factoryMap.containsKey(factory.getType())) {
            throw new IllegalStateException("Factory for type '" + factory.getType() + "' already exists");
        }
        factoryMap.put(factory.getType(), factory);
    }

    @Override
    public void removeFactory(String type) {
        factoryMap.remove(type);
    }
}
