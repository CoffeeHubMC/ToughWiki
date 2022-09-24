package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.commands.wiki.WikiCommand;

import java.util.Objects;
import java.util.logging.Logger;

public class PaperCommandRegisterTask extends BootstrapTask {
    private final ToughWiki toughWiki;

    public PaperCommandRegisterTask(ToughWiki toughWiki) {
        super("paperCommandRegister", Phase.POST_CONFIG);
        this.toughWiki = toughWiki;
    }

    @Override
    public void run(Logger logger) {
        Objects.requireNonNull(toughWiki.getCommand("wiki")).setExecutor(new WikiCommand());
    }
}
