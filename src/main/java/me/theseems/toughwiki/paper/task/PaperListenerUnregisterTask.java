package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import org.bukkit.event.HandlerList;

import java.util.logging.Logger;

public class PaperListenerUnregisterTask extends BootstrapTask {
    public PaperListenerUnregisterTask() {
        super("paperListenerUnregister", Phase.SHUTDOWN);
    }

    @Override
    public void run(Logger logger) {
        HandlerList.unregisterAll(PaperListenerRegisterTask.tabCompleteListener);
        HandlerList.unregisterAll(PaperListenerRegisterTask.playerLeaveListener);
    }
}
