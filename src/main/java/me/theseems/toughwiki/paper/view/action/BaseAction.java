package me.theseems.toughwiki.paper.view.action;

import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;

public class BaseAction implements Action {
    private TriggerType triggerType;

    public BaseAction(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    @Override
    public TriggerType getTriggerType() {
        return triggerType;
    }
}
