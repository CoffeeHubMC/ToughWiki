package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.view.IFWikiPageView;

import java.util.logging.Logger;

public class PaperRegisterViewTask extends BootstrapTask {
    public PaperRegisterViewTask() {
        super("paperRegisterViews", Phase.POST_CONFIG);
    }

    @Override
    public void run(Logger logger) {
        for (WikiPage page : ToughWikiAPI.getInstance().getPageRepository().getAllPages()) {
            ToughWikiAPI.getInstance().getViewManager().store(new IFWikiPageView(page.getName()));
        }
    }
}
