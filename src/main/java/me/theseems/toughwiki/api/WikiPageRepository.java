package me.theseems.toughwiki.api;

import java.util.Collection;
import java.util.Optional;

public interface WikiPageRepository {
    void store(WikiPage wikiPage);
    void dispose(String name);
    void clear();

    Collection<WikiPage> getAllPages();

    Optional<WikiPage> getPage(String name);
}
