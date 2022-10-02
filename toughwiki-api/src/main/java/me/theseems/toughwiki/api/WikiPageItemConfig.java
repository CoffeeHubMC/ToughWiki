package me.theseems.toughwiki.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WikiPageItemConfig implements Cloneable {
    private String type;
    private String title;
    private List<String> lore;
    @JsonUnwrapped
    private Map<String, JsonNode> modifiers;

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getLore() {
        return lore;
    }

    @JsonAnySetter
    public void add(String key, JsonNode value) {
        if (modifiers == null) {
            modifiers = new HashMap<>();
        }
        modifiers.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, JsonNode> getModifiers() {
        return modifiers;
    }

    @Override
    public String toString() {
        return "WikiPageItemConfig{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", lore=" + lore +
                ", modifiers=" + modifiers +
                '}';
    }

    @Override
    public WikiPageItemConfig clone() {
        try {
            WikiPageItemConfig clone = (WikiPageItemConfig) super.clone();
            clone.modifiers = new HashMap<>();
            clone.modifiers.putAll(modifiers);
            clone.type = type;
            clone.lore = lore;
            clone.title = title;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
