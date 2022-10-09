package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.variety.GotoAction;

import java.util.Optional;

public class GotoActionProducer implements ActionProducer {
    @Override
    public Optional<Action> produce(TriggerType triggerType, ObjectNode context) {
        if (!context.has("goto")) {
            return Optional.empty();
        }

        return Optional.of(new GotoAction(triggerType, context.get("goto").asText()));
    }
}
