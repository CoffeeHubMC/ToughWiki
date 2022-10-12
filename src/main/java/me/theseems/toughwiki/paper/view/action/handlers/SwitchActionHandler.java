package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.node.IntNode;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.IFWikiPageView;
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.SwitchAction;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class SwitchActionHandler extends BaseWikiPageActionHandler<SwitchAction> {
    public SwitchActionHandler() {
        super(SwitchAction.class);
    }

    @Override
    protected boolean check(SwitchAction action, ActionSender sender) {
        return sender.getContainer().<InventoryInteractEvent, IFWikiPageView, Integer, ChestGui>
                exists("event", "view", "slot", "chestGUI");
    }

    @Override
    protected void handle(SwitchAction action, ActionSender sender) {
        InventoryInteractEvent event = sender.getContainer().getValue("event");
        IFWikiPageView view = sender.getContainer().getValue("view");
        int slot = sender.getContainer().getValue("slot");
        ChestGui chestGui = sender.getContainer().getValue("chestGUI");

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = ((Player) event.getWhoClicked()).getPlayer();
        WikiPageItemConfig ref = view.getRef(action.getReference());
        if (ref == null) {
            throw new IllegalStateException("Could not find item by ref: '" + action.getReference() + "'");
        }

        WikiPageItemConfig clone = ref.clone();
        clone.getModifiers().put("slot", new IntNode(slot));

        GuiItem item = view.makeItem(player, chestGui, clone, slot);
        ((IFWikiActionSender) sender).replaceGUIItem(slot, item);
    }
}
