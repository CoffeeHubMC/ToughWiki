package me.theseems.toughwiki.impl.bootstrap.tasks;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;

import java.util.logging.Logger;

public class PageViewDisposeTask extends BootstrapTask {
    public PageViewDisposeTask() {
        super("pageDispose", Phase.SHUTDOWN);
    }

    @Override
    public void run(Logger logger) {
        ToughWikiAPI.getInstance()
                .getViewManager()
                .getAllViews()
                .forEach(view -> ToughWikiAPI.getInstance()
                        .getViewManager()
                        .dispose(view.getPage().getName()));
    }
}
