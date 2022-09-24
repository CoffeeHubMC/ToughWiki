package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;

import java.util.Collection;

public class WikiListPagesCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "shows a list of all the wiki pages there are";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.listpages";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Collection<WikiPage> pages = ToughWikiAPI.getInstance().getPageRepository().getAllPages();
        sender.sendMessage(TextUtils.parse("&fThere are(is) " + pages.size() + " page(s)"));
        for (WikiPage allPage : pages) {
            sender.sendMessage(TextUtils.parse("&f" + allPage.getName() + " ")
                    .append(TextUtils.parse("&b&l[OPEN]").clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.RUN_COMMAND,
                            "/wiki page " + allPage.getName()))));
        }
    }
}
