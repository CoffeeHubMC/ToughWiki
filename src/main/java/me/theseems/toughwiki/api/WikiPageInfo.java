package me.theseems.toughwiki.api;

import java.util.List;

public class WikiPageInfo {
    private String title;
    private int size;
    private List<WikiPageItemConfig> items;

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public List<WikiPageItemConfig> getItems() {
        return items;
    }
}
