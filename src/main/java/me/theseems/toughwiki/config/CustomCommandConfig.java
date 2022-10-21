package me.theseems.toughwiki.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;
import java.util.Map;

public class CustomCommandConfig {
    @JsonUnwrapped
    private Map<String, ToughWikiCommandConfig> commands;

    @JsonAnyGetter
    public Map<String, ToughWikiCommandConfig> getCommands() {
        return commands;
    }

    @JsonAnySetter
    public void add(String key, ToughWikiCommandConfig command) {
        if (commands == null) {
            commands = new HashMap<>();
        }
        commands.put(key, command);
    }
}
