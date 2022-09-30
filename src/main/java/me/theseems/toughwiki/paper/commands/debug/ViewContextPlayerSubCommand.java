package me.theseems.toughwiki.paper.commands.debug;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ViewContextPlayerSubCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "viewplayercontext";
    }

    @Override
    public String getDescription() {
        return "shows info about specific view";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.debug.viewplayercontext";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextUtils.parse("&7Please, specify the name of the page AND player's name"));
            return;
        }

        String pageName = args[0];
        ToughWikiAPI api = ToughWikiAPI.getInstance();

        Optional<WikiPageView> wikiPageView = api.getPageRepository()
                .getPage(pageName)
                .flatMap(ToughWikiAPI.getInstance().getViewManager()::getView);

        if (wikiPageView.isEmpty()) {
            sender.sendMessage(TextUtils.parse("&7Requested page and/or wiki does not exist"));
            return;
        }

        String playerName = args[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(TextUtils.parse("&7Requested player either does not exist or is offline"));
            return;
        }

        WikiPageView view = wikiPageView.get();
        String context = view.getContext(player.getUniqueId()).toPrettyString();

        sender.sendMessage("^\n" + context + "\n$");
    }
}
