package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WikiReloadCommand implements SubCommand {
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
        ToughWiki.getBootstrap().execute(Phase.SHUTDOWN, Phase.CONFIG, Phase.POST_CONFIG);
        sender.sendMessage(TextUtils.parse("&bConfiguration reloaded"));
    }
}
