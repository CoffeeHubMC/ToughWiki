package me.theseems.toughwiki.paper.commands.toughwiki;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.command.CommandSender;

public class WikiReloadSubCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads plugin's configuration";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.reload";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ToughWiki.getBootstrap().execute(Phase.SHUTDOWN, Phase.CONFIG, Phase.POST_CONFIG)) {
            sender.sendMessage(TextUtils.parse("&bConfiguration reloaded"));
        } else {
            sender.sendMessage(TextUtils.parse("&cFailed to reload the configuration. Please, check logs."));
        }
    }
}
