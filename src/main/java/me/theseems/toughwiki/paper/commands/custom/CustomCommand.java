package me.theseems.toughwiki.paper.commands.custom;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.config.ToughWikiCommandConfig;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomCommand extends BukkitCommand {
    private final String label;
    private final ToughWikiCommandConfig commandConfig;

    public CustomCommand(String label, ToughWikiCommandConfig commandConfig) {
        super(label,
                "Custom ToughWiki page command",
                commandConfig.getPages() == null || commandConfig.getPages().size() == 1
                        ? ""
                        : String.join("|", commandConfig.getPages()),
                Optional.ofNullable(commandConfig.getAliases())
                        .map(aliases -> aliases.stream().toList())
                        .orElse(Collections.emptyList()));
        this.label = label;
        this.commandConfig = commandConfig;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only ingame usage");
            return true;
        }

        if (commandConfig.getPermission() != null && !sender.hasPermission(commandConfig.getPermission())) {
            return true;
        }

        Set<String> originalPages = Optional
                .ofNullable(commandConfig.getPages())
                .orElseThrow(() -> new IllegalStateException("Custom command " + label + " does not include any pages"));
        Set<String> possiblePages = originalPages.stream()
                .filter(s -> player.hasPermission("toughwiki.command.showpage." + s))
                .collect(Collectors.toSet());

        if (possiblePages.isEmpty()) {
            return true;
        }

        String page;
        if (possiblePages.size() != 1) {
            if (args.length == 0 || !possiblePages.contains(args[0])) {
                sender.sendMessage(TextUtils.parse("&7Click on the page you'd like to open:"));
                for (String possiblePage : possiblePages) {
                    sender.sendMessage(
                            TextUtils.parse("&7" + possiblePage + " &b&l[OPEN]")
                                    .hoverEvent(HoverEvent.showText(TextUtils.parse("&bClick here to open " + possiblePage)))
                                    .clickEvent(ClickEvent.runCommand("/" + label + " " + possiblePage))
                    );
                }
                return true;
            }
            page = args[0];
        } else {
            page = possiblePages.iterator().next();
        }

        Bukkit.getScheduler().runTask(ToughWiki.getPlugin(), () -> player.chat("/wiki " + page));
        return true;
    }
}
