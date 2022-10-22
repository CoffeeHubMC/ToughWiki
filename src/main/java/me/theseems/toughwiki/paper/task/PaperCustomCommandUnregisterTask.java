package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.commands.custom.CustomCommandManager;

import java.util.Optional;
import java.util.logging.Logger;

public class PaperCustomCommandUnregisterTask extends BootstrapTask {
    public PaperCustomCommandUnregisterTask() {
        super("customCommandUnregister", Phase.SHUTDOWN);
    }

    @Override
    public void run(Logger logger) {
        Optional.ofNullable(ToughWiki.getCommandManager())
                .ifPresent(CustomCommandManager::dispose);
    }
}
