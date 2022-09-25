package me.theseems.toughwiki.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.theseems.toughwiki.api.WikiPageInfo;

import java.util.HashMap;
import java.util.Map;

public class FlatToughWikiConfig {
    @JsonUnwrapped
    private Map<String, ToughWikiConfig> pages;

    @JsonAnyGetter
    public Map<String, ToughWikiConfig> getPages() {
        return pages;
    }

    @JsonAnySetter
    public void add(String key, ToughWikiConfig value) {
        if (pages == null) {
            pages = new HashMap<>();
        }
        pages.put(key, value);
    }

    @Override
    public String toString() {
        return "FlatToughWikiConfig{" +
                "pages=" + pages +
                '}';
    }
}
