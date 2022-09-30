package me.theseems.toughwiki.paper.commands.debug;

import me.theseems.toughwiki.paper.commands.CommandContainer;
import me.theseems.toughwiki.paper.commands.SubCommand;
import org.bukkit.command.CommandSender;

public class WikiDebugSubCommandContainer extends CommandContainer implements SubCommand {
    @Override
    public String getLabel() {
        return "debug";
    }

    @Override
    public String getDescription() {
        return "the debug root command";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.debug";
    }

    public WikiDebugSubCommandContainer() {
        add(new GeneralInfoSubCommand());
        add(new ViewInfoSubCommand());
        add(new ViewContextPlayerSubCommand());
        add(new DumpConfigSubCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        onCommand(sender, null, "toughwiki debug", args);
    }
}
