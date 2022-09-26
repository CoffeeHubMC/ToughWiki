package me.theseems.toughwiki.paper.view.action;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.paper.view.IFWikiPageView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IFWikiActionSender implements ActionSender {
    private final IFWikiPageView view;
    private final WikiPageItemConfig wikiPageItemConfig;
    private final ChestGui chestGui;
    private final int slot;
    private final GuiItem guiItem;
    private final InventoryClickEvent event;

    public IFWikiActionSender(IFWikiPageView view, WikiPageItemConfig wikiPageItemConfig, ChestGui chestGui, GuiItem guiItem, int slot, InventoryClickEvent event) {
        this.view = view;
        this.wikiPageItemConfig = wikiPageItemConfig;
        this.chestGui = chestGui;
        this.guiItem = guiItem;
        this.slot = slot;
        this.event = event;
    }

    @Override
    public WikiPageItemConfig getItemConfig() {
        return wikiPageItemConfig;
    }

    @Override
    public IFWikiPageView getView() {
        return view;
    }

    public WikiPageItemConfig getWikiPageItemConfig() {
        return wikiPageItemConfig;
    }

    public ChestGui getChestGui() {
        return chestGui;
    }

    public int getSlot() {
        return slot;
    }

    public ObjectNode getContext() {
        return view.getContext(event.getWhoClicked().getUniqueId());
    }

    public ItemStack getItemStack() {
        return chestGui.getInventoryComponent().getItem(slot % 9, slot / 9);
    }

    public void updateItemStack(ItemMeta meta) {
        chestGui.getItems().stream()
                .filter(item -> item.equals(guiItem))
                .forEach(guiItem -> guiItem.getItem().setItemMeta(meta));
        chestGui.update();
    }

    public InventoryClickEvent getEvent() {
        return event;
    }
}
