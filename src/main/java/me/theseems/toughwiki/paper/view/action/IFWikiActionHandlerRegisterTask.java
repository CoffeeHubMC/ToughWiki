package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.view.action.handlers.*;

import java.util.List;
import java.util.logging.Logger;

public class IFWikiActionHandlerRegisterTask extends BootstrapTask {
    public IFWikiActionHandlerRegisterTask() {
        super("IFActionHandlerRegister", Phase.PRE_CONFIG);
    }

    @Override
    public void run(Logger logger) throws Exception {
        List.of(new BackActionHandler(),
                        new CloseActionHandler(),
                        new CommandActionHandler(),
                        new ConsoleCommandActionHandler(),
                        new GotoActionHandler(),
                        new SwitchActionHandler(),
                        new SoundHandler())
                .forEach(ToughWikiAPI.getInstance().getActionEmitter()::register);
    }
}
