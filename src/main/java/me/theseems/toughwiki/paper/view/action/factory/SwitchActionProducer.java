package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.variety.SwitchAction;

import java.util.Optional;

public class SwitchActionProducer implements ActionProducer {
    @Override
    public Optional<Action> produce(TriggerType triggerType, ObjectNode context) {
        if (!context.has("switchTo")) {
            return Optional.empty();
        }

        return Optional.of(new SwitchAction(triggerType, context.get("switchTo").asText()));
    }
}
