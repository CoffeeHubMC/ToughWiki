package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.variety.CloseAction;

import java.util.Optional;

public class CloseActionProducer implements ActionProducer {
    @Override
    public Optional<Action> produce(TriggerType triggerType, ObjectNode context) {
        if (!context.has("action")) {
            return Optional.empty();
        }

        String text = context.get("action").asText();
        if (!text.equalsIgnoreCase("close")) {
            return Optional.empty();
        }

        return Optional.of(new CloseAction(triggerType));
    }
}
