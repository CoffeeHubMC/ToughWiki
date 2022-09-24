package me.theseems.toughwiki.impl.bootstrap.tasks;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;

import java.util.logging.Logger;

public class PageClearTask extends BootstrapTask {
    public PageClearTask() {
        super("pageClear", Phase.SHUTDOWN);
    }

    @Override
    public void run(Logger logger) {
        ToughWikiAPI.getInstance().getPageRepository().clear();
    }
}
