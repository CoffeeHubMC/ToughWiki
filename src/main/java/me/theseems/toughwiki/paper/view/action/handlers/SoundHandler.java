package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;

import java.util.Optional;

public class SoundHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        boolean muteMenu = Optional.ofNullable(sender.getView().getGlobalContext())
                .map(context -> context.get("mute"))
                .filter(JsonNode::isBoolean)
                .map(JsonNode::asBoolean)
                .orElse(false);

        boolean muteItem = Optional.ofNullable(sender.getItemConfig())
                .map(WikiPageItemConfig::getModifiers)
                .map(context -> context.get("mute"))
                .filter(JsonNode::isBoolean)
                .map(JsonNode::asBoolean)
                .orElse(false);

        return !muteMenu && !muteItem;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        HumanEntity opener = sender.getEvent().getWhoClicked();
        opener.playSound(getSound(sender.getItemConfig()));
    }

    private net.kyori.adventure.sound.Sound getSound(WikiPageItemConfig config) {
        return getOptionalSound(config)
                .orElse(Sound.sound(
                        Key.key("ui.button.click"),
                        Sound.Source.MASTER,
                        getVolume(config).floatValue(),
                        1f));
    }

    private Optional<Sound> getOptionalSound(WikiPageItemConfig config) {
        return Optional.ofNullable(config.getModifiers().get("sound"))
                .filter(JsonNode::isTextual)
                .map(JsonNode::asText)
                .map(Key::key)
                .map(key -> Sound.sound(key, Sound.Source.MASTER, getVolume(config).floatValue(), 1f));
    }

    private Double getVolume(WikiPageItemConfig config) {
        return Optional.ofNullable(config.getModifiers().get("volume"))
                .filter(JsonNode::isNumber)
                .map(JsonNode::asDouble)
                .orElse(1.0D);
    }
}
