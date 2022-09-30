package me.theseems.toughwiki.paper.commands.debug;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BaseJsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.WikiPageInfo;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.WikiPageView;
import me.theseems.toughwiki.paper.commands.SubCommand;
import me.theseems.toughwiki.paper.view.IFWikiPageView;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class ViewInfoSubCommand implements SubCommand {
    @Override
    public String getLabel() {
        return "pageinfo";
    }

    @Override
    public String getDescription() {
        return "shows info about page and it's view";
    }

    @Override
    public String getPermission() {
        return "toughwiki.command.debug.pageinfo";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(TextUtils.parse("&7Please, specify the name of the page"));
            return;
        }

        String pageName = args[0];
        ToughWikiAPI api = ToughWikiAPI.getInstance();

        Optional<WikiPage> wikiPage = api.getPageRepository().getPage(pageName);
        if (wikiPage.isEmpty()) {
            sender.sendMessage(TextUtils.parse("&7Requested page does not exist."));
            return;
        }

        WikiPage actualWikiPage = wikiPage.get();
        WikiPageInfo info = actualWikiPage.getInfo();
        sender.sendMessage(TextUtils.parse("&bModifiers:&f \n^\n" + Optional.ofNullable(info.getModifiers())
                .map(BaseJsonNode::toPrettyString)
                .orElse("<empty>") + "\n$"));
        sender.sendMessage(TextUtils.parse("&bCount of items:&f " + info.getItems().size()));

        for (WikiPageItemConfig item : info.getItems()) {
            Component component;
            try {
                component = TextUtils.parse(item.getTitle() == null ? "<empty>" : item.getTitle())
                        .hoverEvent(HoverEvent.showText(TextUtils.parse(
                                "&btype: %s\n&7^\n%s$".formatted(
                                        item.getType(),
                                        item.getModifiers() == null
                                                ? "<empty>"
                                                : new ObjectMapper(new YAMLFactory()).writeValueAsString(item.getModifiers())))));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            sender.sendMessage(TextUtils.parse("&b&l[&f")
                    .append(component)
                    .append(TextUtils.parse("&b&l] ")));
        }

        Optional<WikiPageView> view = api.getViewManager().getView(wikiPage.get());
        if (view.isEmpty()) {
            sender.sendMessage(TextUtils.parse(
                    "&7There is no registered view for the page '" + actualWikiPage.getName() + "'"));
            return;
        }

        WikiPageView actualView = view.get();
        sender.sendMessage(TextUtils.parse("&bView class:&f " + actualView.getClass()));
        sender.sendMessage(TextUtils.parse("&bGlobal context:&f \n^\n%s\n$"
                .formatted(actualView.getGlobalContext().toPrettyString())));

        if (actualView instanceof IFWikiPageView) {
            sender.sendMessage(TextUtils.parse("&bPlayer GUI map: &f^\n")
                    .append(Component.text(((IFWikiPageView) actualView).getPlayerGUIMap().toString()))
                    .append(TextUtils.parse("\n$")));
        }
    }
}
