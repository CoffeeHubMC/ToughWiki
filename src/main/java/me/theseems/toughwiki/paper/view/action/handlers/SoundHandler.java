package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.action.handlers.base.InventoryEventActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.SoundAction;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class SoundHandler extends InventoryEventActionHandler<SoundAction> {
    public SoundHandler() {
        super(SoundAction.class);
    }

    @Override
    protected void handle(SoundAction action, ActionSender sender, InventoryInteractEvent event) {
        HumanEntity opener = event.getWhoClicked();
        opener.playSound(Sound.sound(action.getKey(), Sound.Source.AMBIENT, action.getVolume(), action.getPitch()));
    }
}
