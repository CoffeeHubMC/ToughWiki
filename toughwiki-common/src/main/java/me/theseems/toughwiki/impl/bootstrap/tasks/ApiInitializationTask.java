package me.theseems.toughwiki.impl.bootstrap.tasks;

import me.theseems.toughwiki.impl.SimpleWikiPageRepository;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.impl.SimpleActionEmitter;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.view.SimpleWikiPageViewManager;

import java.util.logging.Logger;

public class ApiInitializationTask extends BootstrapTask {
    public ApiInitializationTask() {
        super("apiInit", Phase.PRE_CONFIG);
    }

    @Override
    public void run(Logger logger) {
        new ToughWikiAPI(new SimpleWikiPageViewManager(), new SimpleWikiPageRepository(), new SimpleActionEmitter());
    }
}
