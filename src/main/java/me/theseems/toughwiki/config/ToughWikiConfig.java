package me.theseems.toughwiki.config;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.theseems.toughwiki.api.WikiPageInfo;

import java.util.Map;

public class ToughWikiConfig {
    private Map<String, ToughWikiConfig> pages;
    private String parent;

    @JsonUnwrapped
    private WikiPageInfo content;

    public Map<String, ToughWikiConfig> getPages() {
        return pages;
    }

    public String getParent() {
        return parent;
    }

    public WikiPageInfo getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "ToughWikiConfig{" +
                "pages=" + pages +
                ", parent='" + parent + '\'' +
                ", content=" + content +
                '}';
    }
}
