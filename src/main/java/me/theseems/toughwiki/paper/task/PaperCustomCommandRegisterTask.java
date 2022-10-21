package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.commands.custom.CustomCommandManager;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class PaperCustomCommandRegisterTask extends BootstrapTask {
    private final Consumer<CustomCommandManager> consumer;

    public PaperCustomCommandRegisterTask(Consumer<CustomCommandManager> consumer) {
        super("customCommandRegister", Phase.POST_CONFIG);
        this.consumer = consumer;
    }

    @Override
    public void run(Logger logger) {
        CustomCommandManager customCommandManager = new CustomCommandManager(ToughWiki.getCommandConfig().getCommands());
        customCommandManager.setup();
        consumer.accept(customCommandManager);
    }
}
