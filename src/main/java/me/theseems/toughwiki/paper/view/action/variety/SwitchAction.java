package me.theseems.toughwiki.paper.view.action.variety;

import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.BaseAction;

public class SwitchAction extends BaseAction {
    private final String reference;

    public SwitchAction(TriggerType triggerType, String reference) {
        super(triggerType);
        this.reference = reference;
    }

    public String getReference() {
        return reference;
    }
}
