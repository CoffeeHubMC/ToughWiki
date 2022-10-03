package me.theseems.toughwiki.paper.commands.toughwiki;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class WikiForceShowSubCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "forceshow";
    }

    @Override
    public String getDescription() {
        return "shows a certain wiki page to a player";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.forceshow";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextUtils.parse("&7Please, specify both page and player"));
            return;
        }

        Optional<WikiPageView> wikiPageView = ToughWikiAPI.getInstance()
                .getPageRepository()
                .getPage(args[0])
                .flatMap(page -> ToughWikiAPI.getInstance().getViewManager().getView(page));

        if (wikiPageView.isEmpty()) {
            sender.sendMessage(TextUtils.parse("There is no page/view: '" + args[0] + "'"));
            return;
        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(TextUtils.parse(args[1] + " is offline"));
            return;
        }

        wikiPageView.get().show(player.getUniqueId());
        sender.sendMessage(TextUtils.parse("&7Successfully shown " + args[0] + " to " + args[1]));
    }
}
