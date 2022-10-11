package me.theseems.toughwiki.api.view;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.component.ComponentContainer;

public interface ActionSender {
    WikiPageItemConfig getItemConfig();

    WikiPageView getView();

    ComponentContainer getContainer();
}
