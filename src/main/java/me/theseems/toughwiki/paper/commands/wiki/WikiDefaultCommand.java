package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WikiDefaultCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ingame use only. Try using /toughtwiki instead.");
            return false;
        }

        String wikiPageName = args.length == 0 ? "default" : args[0];
        if (!sender.hasPermission("toughwiki.command.showpage." + wikiPageName)) {
            sendDeny(sender);
            return true;
        }

        ToughWikiAPI.getInstance()
                .getPageRepository()
                .getPage(wikiPageName)
                .flatMap(page -> ToughWikiAPI.getInstance().getViewManager().getView(page))
                .ifPresentOrElse(
                        wikiPageView -> wikiPageView.show(((Player) sender).getUniqueId()),
                        () -> sendDeny(sender));

        return true;
    }

    private void sendDeny(CommandSender sender) {
        sender.sendMessage(TextUtils.parse("&7Sorry, but the wiki page is missing or inaccessible."));
    }
}
