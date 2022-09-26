package me.theseems.toughwiki.api;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class WikiPageInfo {
    private String title;
    private int size;
    private List<WikiPageItemConfig> items;

    private ObjectNode modifiers;

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public List<WikiPageItemConfig> getItems() {
        return items;
    }

    public ObjectNode getModifiers() {
        return modifiers;
    }
}
