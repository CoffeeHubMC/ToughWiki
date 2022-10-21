package me.theseems.toughwiki.paper.commands.custom;

import me.theseems.toughwiki.config.ToughWikiCommandConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

public class CustomCommandManager {
    private final Map<String, ToughWikiCommandConfig> commandConfigMap;
    private CommandMap commandMap;

    public CustomCommandManager(Map<String, ToughWikiCommandConfig> commandConfigMap) {
        this.commandConfigMap = commandConfigMap;
    }

    public void setup() {
        this.commandMap = getCommandMap();
        commandConfigMap.forEach((s, toughWikiCommandConfig) -> {
            CustomCommand customCommand = new CustomCommand(s, toughWikiCommandConfig);
            commandMap.register(s, customCommand);
            commandMap.getKnownCommands().put(s, customCommand);
        });
    }

    public void dispose() {
        for (String label : commandConfigMap.keySet()) {
            Optional.ofNullable(commandMap.getCommand(label))
                    .ifPresent((command) -> {
                        command.unregister(commandMap);
                        commandMap.getKnownCommands().remove(command.getLabel());
                        for (String alias : command.getAliases()) {
                            commandMap.getKnownCommands().remove(alias);
                        }
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
