package me.theseems.toughwiki.config;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.theseems.toughwiki.api.WikiPageInfo;

import java.util.Map;

public class ToughWikiConfig {
    private Map<String, ToughWikiConfig> pages;
    private Map<String, Object> modifiers;
    private String parent;

    @JsonUnwrapped
    private WikiPageInfo content;

    public Map<String, ToughWikiConfig> getPages() {
        return pages;
    }

    public Map<String, Object> getModifiers() {
        return modifiers;
    }

    public String getParent() {
        return parent;
    }

    public WikiPageInfo getContent() {
        return content;
    }
}
