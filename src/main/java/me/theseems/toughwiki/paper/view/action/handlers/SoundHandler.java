package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.InteractEventWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.SoundAction;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;

public class SoundHandler extends BaseWikiPageActionHandler<InteractEventWikiActionSender, SoundAction> {
    public SoundHandler() {
        super(InteractEventWikiActionSender.class, SoundAction.class);
    }

    @Override
    protected void handle(SoundAction action, InteractEventWikiActionSender sender) {
        HumanEntity opener = sender.getEvent().getWhoClicked();
        opener.playSound(Sound.sound(action.getKey(), Sound.Source.AMBIENT, action.getVolume(), action.getPitch()));
    }
}
