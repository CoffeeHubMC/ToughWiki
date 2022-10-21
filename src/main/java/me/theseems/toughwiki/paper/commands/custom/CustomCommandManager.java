package me.theseems.toughwiki.paper.commands.custom;

import me.theseems.toughwiki.config.ToughWikiCommandConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class CustomCommandManager {
    private final Map<String, ToughWikiCommandConfig> commandConfigMap;

    public CustomCommandManager(Map<String, ToughWikiCommandConfig> commandConfigMap) {
        this.commandConfigMap = commandConfigMap;
    }

    public void setup() {
        commandConfigMap.forEach((s, toughWikiCommandConfig) -> {
            CustomCommand customCommand = new CustomCommand(s, toughWikiCommandConfig);
            getCommandMap().register(s, customCommand);
            getCommandMap().getKnownCommands().put(s, customCommand);
        });
    }

    public void dispose() {
        for (String label : commandConfigMap.keySet()) {
            Optional.ofNullable(getCommandMap().getCommand(label))
                    .ifPresent((command) -> {
                        command.unregister(getCommandMap());
                        getCommandMap().getKnownCommands().remove(label);
                    });
        }
    }

    private CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            Field f = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            commandMap = (CommandMap) f.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
            e.printStackTrace();
        }
        return commandMap;
    }

}
