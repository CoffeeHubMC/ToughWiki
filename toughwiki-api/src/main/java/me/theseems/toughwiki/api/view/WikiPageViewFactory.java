package me.theseems.toughwiki.api.view;

import me.theseems.toughwiki.api.WikiPage;

import java.util.Optional;

public interface WikiPageViewFactory {
    String getType();

    Optional<WikiPageView> produce(WikiPage page, String name);
}
