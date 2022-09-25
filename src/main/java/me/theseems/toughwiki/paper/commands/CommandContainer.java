package me.theseems.toughwiki.paper.commands;

import me.theseems.toughwiki.BuildConstants;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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

        if (!sender.hasPermission(subCommandMap.get(args[0]).getPermission())) {
            showBanner(sender);
            return true;
        }

        String[] mappedArgs = Arrays.stream(args).skip(1).toArray(String[]::new);
        subCommandMap.get(args[0].toLowerCase()).execute(sender, mappedArgs);

        return true;
    }

    protected void showHelp(Command command, CommandSender sender) {
        showBanner(sender);
        for (SubCommand value : subCommandMap.values()) {
            if (sender.hasPermission(value.getPermission())) {
                sender.sendMessage(TextUtils
                        .parse("&b/" + command.getLabel() + " " + value.getLabel() + " &7- &f" + value.getDescription())
                        .clickEvent(ClickEvent.suggestCommand("/" + command.getLabel() + " " + value.getLabel())));
            }
        }
    }

    protected void showBanner(CommandSender sender) {
        sender.sendMessage(LegacyComponentSerializer.legacyAmpersand()
                .deserialize("&b&lToughWiki &rby TheSeems<me@theseems.ru> &7v%s".formatted(BuildConstants.VERSION)));
    }

    protected void add(SubCommand subCommand) {
        if (subCommandMap.containsKey(subCommand.getLabel())) {
            throw new IllegalStateException("Command container already contains a subcommand named '" + subCommand.getLabel() + "'");
        }
        subCommandMap.put(subCommand.getLabel(), subCommand);
    }
}
