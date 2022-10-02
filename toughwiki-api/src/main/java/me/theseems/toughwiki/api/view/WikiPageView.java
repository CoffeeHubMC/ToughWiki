package me.theseems.toughwiki.api.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.api.WikiPage;

import java.util.UUID;

public interface WikiPageView {
    WikiPage getPage();

    String getName();

    void show(UUID player);

    void dispose(UUID player);

    void dispose();

    JsonNode getContext(UUID player);

    JsonNode getGlobalContext();

    default <T> T contextAs(UUID player, String key, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            if (key == null) {
                return mapper.readValue(getContext(player).toString(), tClass);
            }
            return mapper.readValue(getContext(player).get(key).toString(), tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T contextAs(UUID player, Class<T> tClass) {
        return contextAs(player, null, tClass);
    }
}
