package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.paper.view.IFWikiPageView;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class WikiDebugCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "debug";
    }

    @Override
    public String getDescription() {
        return "shows debug information";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.debug";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Collection<WikiPageView> views = ToughWikiAPI.getInstance().getViewManager().getAllViews();
        sender.sendMessage(TextUtils.parse("&fThere are " + views.size() + " view(s)"));
        for (WikiPageView view : views) {
            sender.sendMessage(TextUtils.parse("&b%s &7for page %s (\\/\\ %s) &8(impl: %s)"
                    .formatted(view.getName(), view.getPage().getName(), view.getPage().getParent().map(WikiPage::getName).orElse("N/A"), view.getClass().getName())));
        }
        sender.sendMessage(TextUtils.parse("&bCache for IFWikiPageView: " + IFWikiPageView.PER_PLAYER_CACHE_TTL));

        sender.sendMessage(TextUtils.parse("&fThere are %d bootstrap tasks"
                .formatted(ToughWiki.getBootstrap().getBootstrapTasks().size())));

        for (BootstrapTask bootstrapTask : ToughWiki.getBootstrap().getBootstrapTasks()) {
            sender.sendMessage(TextUtils.parse("&f%s &7(@%s)".formatted(bootstrapTask.getName(), bootstrapTask.getPhase())));
        }
    }
}
