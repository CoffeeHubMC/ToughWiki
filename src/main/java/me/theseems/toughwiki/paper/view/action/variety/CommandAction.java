package me.theseems.toughwiki.paper.view.action.variety;

import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.BaseAction;

public class CommandAction extends BaseAction {
    private String commandName;

    public CommandAction(TriggerType triggerType, String commandName) {
        super(triggerType);
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
