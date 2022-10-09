package me.theseems.toughwiki.paper.view.action.variety;

import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.BaseAction;

public class GotoAction extends BaseAction {
    private String gotoName;

    public GotoAction(TriggerType triggerType, String gotoName) {
        super(triggerType);
        this.gotoName = gotoName;
    }

    public String getGotoName() {
        return gotoName;
    }
}
