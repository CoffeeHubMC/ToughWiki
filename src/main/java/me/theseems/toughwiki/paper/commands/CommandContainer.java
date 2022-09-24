package me.theseems.toughwiki.paper.commands;

import me.theseems.toughwiki.BuildConstants;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandContainer implements CommandExecutor {
    private final Map<String, SubCommand> subCommandMap;

    public CommandContainer() {
        subCommandMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0
                || args[0].equalsIgnoreCase("help")
                || !subCommandMap.containsKey(args[0].toLowerCase())) {
            showHelp(command, sender);
            return true;
        }

        String[] mappedArgs = Arrays.stream(args).skip(1).toArray(String[]::new);
        subCommandMap.get(args[0].toLowerCase()).execute(sender, mappedArgs);

        return true;
    }

    protected void showHelp(Command command, CommandSender sender) {
        sender.sendMessage(TextUtils.parse("&b&lTough&f&lWiki &7v" + BuildConstants.VERSION + " &r&fby TheSeems<me@theseems.ru>"));
        for (SubCommand value : subCommandMap.values()) {
            if (sender.hasPermission(value.getPermission())) {
                sender.sendMessage(TextUtils
                        .parse("&b/" + command.getLabel() + " " + value.getLabel() + " &7- &f" + value.getDescription())
                        .clickEvent(ClickEvent.suggestCommand("/" + command.getLabel() + " " + value.getLabel())));
            }
        }
    }

    protected void add(SubCommand subCommand) {
        if (subCommandMap.containsKey(subCommand.getLabel())) {
            throw new IllegalStateException("Command container already contains a subcommand named '" + subCommand.getLabel() + "'");
        }
        subCommandMap.put(subCommand.getLabel(), subCommand);
    }
}
