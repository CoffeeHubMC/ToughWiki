package me.theseems.toughwiki.api;

import java.util.Collection;
import java.util.Optional;

public interface WikiPage {
    Optional<WikiPage> getParent();
    Collection<WikiPage> getChildren();

    WikiPageInfo getInfo();

    String getName();
}
