package me.theseems.toughwiki.api.view;

import me.theseems.toughwiki.api.WikiPage;

import java.util.UUID;

public interface WikiPageView {
    WikiPage getPage();

    String getName();

    void show(UUID player);
    void dispose(UUID player);
}
