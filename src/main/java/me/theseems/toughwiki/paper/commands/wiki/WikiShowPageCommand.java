package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WikiShowPageCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "page";
    }

    @Override
    public String getDescription() {
        return "shows a certain wiki page";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.showpage";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ingame use only");
            return;
        }

        if (args.length == 0 || !sender.hasPermission("toughwiki.command.showpage." + args[0])) {
            sendDeny(sender);
            return;
        }

        Optional<WikiPageView> wikiPageView = ToughWikiAPI.getInstance()
                .getPageRepository()
                .getPage(args[0])
                .flatMap(page -> ToughWikiAPI.getInstance().getViewManager().getView(page));

        if (wikiPageView.isEmpty()) {
            sendDeny(sender);
            return;
        }

        wikiPageView.get().show(((Player) sender).getUniqueId());
    }

    private void sendDeny(CommandSender sender) {
        sender.sendMessage(TextUtils.parse("&7This page does not exist or is inaccessible"));
    }
}
