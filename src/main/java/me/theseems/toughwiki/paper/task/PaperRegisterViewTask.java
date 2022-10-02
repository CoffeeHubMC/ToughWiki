package me.theseems.toughwiki.paper.task;

import com.fasterxml.jackson.databind.JsonNode;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.view.IFWikiPageView;

import java.util.Optional;
import java.util.logging.Logger;

public class PaperRegisterViewTask extends BootstrapTask {
    public PaperRegisterViewTask() {
        super("paperRegisterViews", Phase.POST_CONFIG);
    }

    @Override
    public void run(Logger logger) {
        for (WikiPage page : ToughWikiAPI.getInstance().getPageRepository().getAllPages()) {
            boolean hasNoPendingView = getViewType(page)
                    .map(text -> text.equals("default"))
                    .orElse(true);
            if (hasNoPendingView) {
                IFWikiPageView view = new IFWikiPageView(page.getName(), page.getInfo().getModifiers());
                ToughWikiAPI.getInstance().getViewManager().store(view);
            }
        }
    }

    public static Optional<String> getViewType(WikiPage page) {
        return Optional.ofNullable(page.getInfo().getModifiers())
                .map(modifiers -> modifiers.get("viewType"))
                .filter(JsonNode::isTextual)
                .map(JsonNode::asText);
    }
}
