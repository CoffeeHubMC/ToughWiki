package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.SoundAction;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;

public class SoundHandler extends IFWikiPageActionHandler<SoundAction> {
    public SoundHandler() {
        super(SoundAction.class);
    }

    @Override
    protected void proceed(SoundAction action, IFWikiActionSender sender) {
        HumanEntity opener = sender.getEvent().getWhoClicked();
        opener.playSound(Sound.sound(action.getKey(), Sound.Source.AMBIENT, action.getVolume(), action.getPitch()));
    }
}
