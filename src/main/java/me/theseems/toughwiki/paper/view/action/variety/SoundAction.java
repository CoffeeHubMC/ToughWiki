package me.theseems.toughwiki.paper.view.action.variety;

import me.theseems.toughwiki.api.view.TriggerType;
import me.theseems.toughwiki.paper.view.action.BaseAction;
import net.kyori.adventure.key.Key;

public class SoundAction extends BaseAction {
    private Key key;
    private float volume;
    private float pitch;

    public SoundAction(TriggerType triggerType, Key key, float volume, float pitch) {
        super(triggerType);
        this.key = key;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
