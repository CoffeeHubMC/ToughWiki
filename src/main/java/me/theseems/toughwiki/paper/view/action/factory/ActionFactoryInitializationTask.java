package me.theseems.toughwiki.paper.view.action.factory;

import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class ActionFactoryInitializationTask extends BootstrapTask {
    private final Consumer<ActionFactory> actionFactoryConsumer;

    public ActionFactoryInitializationTask(Consumer<ActionFactory> actionFactoryConsumer) {
        super("initActionFactory", Phase.PRE_CONFIG);
        this.actionFactoryConsumer = actionFactoryConsumer;
    }

    @Override
    public void run(Logger logger) throws Exception {
        ActionFactory actionFactory = new ActionFactory();
        actionFactory.add(new BackActionProducer());
        actionFactory.add(new CloseActionProducer());
        actionFactory.add(new CommandActionProducer());
        actionFactory.add(new GotoActionProducer());
        actionFactory.add(new SoundActionProducer());
        actionFactory.add(new SwitchActionProducer());

        actionFactoryConsumer.accept(actionFactory);
    }
}
