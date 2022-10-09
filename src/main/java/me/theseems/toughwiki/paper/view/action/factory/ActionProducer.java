package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;

import java.util.Optional;

public interface ActionProducer {
    Optional<Action> produce(TriggerType triggerType, ObjectNode context);
}
