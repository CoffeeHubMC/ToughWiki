package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.variety.ConsoleCommandAction;

import java.util.Optional;

public class ConsoleCommandActionProducer implements ActionProducer {
    @Override
    public Optional<Action> produce(TriggerType triggerType, ObjectNode context) {
        if (!context.has("console")) {
            return Optional.empty();
        }

        return Optional.of(new ConsoleCommandAction(triggerType, context.get("console").asText()));
    }
}
