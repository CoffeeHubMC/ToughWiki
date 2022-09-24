package me.theseems.toughwiki.paper.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    String getLabel();
    String getDescription();
    String getPermission();

    void execute(CommandSender sender, String[] args);
}
