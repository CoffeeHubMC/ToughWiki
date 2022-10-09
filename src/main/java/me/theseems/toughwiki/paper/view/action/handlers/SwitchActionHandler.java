package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.node.IntNode;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.SwitchAction;
import org.bukkit.entity.Player;

public class SwitchActionHandler extends IFWikiPageActionHandler<SwitchAction> {
    public SwitchActionHandler() {
        super(SwitchAction.class);
    }

    @Override
    protected void proceed(SwitchAction action, IFWikiActionSender sender) {
        if (!(sender.getEvent().getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = ((Player) sender.getEvent().getWhoClicked()).getPlayer();
        WikiPageItemConfig ref = sender.getView().getRef(action.getReference());
        if (ref == null) {
            throw new IllegalStateException("Could not find item by ref: '" + action.getReference() + "'");
        }

        WikiPageItemConfig clone = ref.clone();
        clone.getModifiers().put("slot", new IntNode(sender.getSlot()));

        GuiItem item = sender.getView().makeItem(player, sender.getChestGui(), clone, sender.getSlot());
        sender.replaceGUIItem(sender.getSlot(), item);
    }
}
