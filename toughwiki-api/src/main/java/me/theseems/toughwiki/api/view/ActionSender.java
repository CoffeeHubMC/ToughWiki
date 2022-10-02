package me.theseems.toughwiki.api.view;

import me.theseems.toughwiki.api.WikiPageItemConfig;

public interface ActionSender {
    WikiPageItemConfig getItemConfig();

    WikiPageView getView();
}
