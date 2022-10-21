package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ActionFactory {
    private final List<ActionProducer> producerList;

    public ActionFactory() {
        this.producerList = new LinkedList<>();
    }

    public Action produce(TriggerType triggerType, ObjectNode context) {
        for (ActionProducer actionProducer : producerList) {
            Optional<Action> result = actionProducer.produce(triggerType, context);
            if (result.isPresent()) {
                return result.get();
            }
        }

        return null;
    }

    public void add(ActionProducer producer) {
        if (producerList.contains(producer)) {
            return;
        }
        producerList.add(producer);
    }

    public List<ActionProducer> getProducerList() {
        return Collections.unmodifiableList(producerList);
    }
}
