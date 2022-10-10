package me.theseems.toughwiki.paper.view.action.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.variety.SoundAction;
import net.kyori.adventure.key.Key;

import java.util.Optional;

public class SoundActionProducer implements ActionProducer {
    @Override
    public Optional<Action> produce(TriggerType triggerType, ObjectNode context) {
        if (!context.has("sound")) {
            return Optional.empty();
        }

        Key soundKey = Key.key(context.get("sound").asText());
        float volume = Optional.ofNullable(context.get("volume"))
                .map(JsonNode::asDouble)
                .map(Double::floatValue)
                .orElse(1f);
        float pitch = Optional.ofNullable(context.get("pitch"))
                .map(JsonNode::asDouble)
                .map(Double::floatValue)
                .orElse(1f);

        return Optional.of(new SoundAction(triggerType, soundKey, volume, pitch));
    }
}
