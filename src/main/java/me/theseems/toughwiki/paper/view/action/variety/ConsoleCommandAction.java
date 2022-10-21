package me.theseems.toughwiki.paper.view.action.variety;

import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.BaseAction;

public class ConsoleCommandAction extends BaseAction {
    private final String commandName;

    public ConsoleCommandAction(TriggerType triggerType, String commandName) {
        super(triggerType);
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
