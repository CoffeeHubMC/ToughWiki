package me.theseems.toughwiki.paper.view.action.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.api.component.ComponentContainer;
import me.theseems.toughwiki.api.view.ActionSender;
import me.theseems.toughwiki.impl.component.SimpleComponentContainer;
import me.theseems.toughwiki.paper.view.IFWikiPageView;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IFWikiActionSender implements ActionSender {
    private final ComponentContainer container;

    public IFWikiActionSender(IFWikiPageView view, WikiPageItemConfig wikiPageItemConfig, ChestGui chestGui, GuiItem guiItem, int slot, InventoryClickEvent event) {
        this.container = new SimpleComponentContainer();
        container.storeValue("view", view);
        container.storeValue("itemConfig", wikiPageItemConfig);
        container.storeValue("chestGUI", chestGui);
        container.storeValue("guiItem", guiItem);
        container.storeValue("slot", slot);
        container.storeValue("event", event);
    }

    @Override
    public WikiPageItemConfig getItemConfig() {
        return container.getValue("itemConfig");
    }

    @Override
    public IFWikiPageView getView() {
        return container.getValue("view");
    }

    @Override
    public ComponentContainer getContainer() {
        return container;
    }

    public WikiPageItemConfig getWikiPageItemConfig() {
        return container.getValue("itemConfig");
    }

    public GuiItem getGUIItem() {
        return container.getValue("guiItem");
    }

    public ChestGui getChestGui() {
        return container.getValue("chestGUI");
    }

    public InventoryClickEvent getEvent() {
        return container.getValue("event");
    }

    public int getSlot() {
        return container.getValue("slot");
    }

    public ObjectNode getContext() {
        return getView().getContext(getEvent().getWhoClicked().getUniqueId());
    }

    public ItemStack getItemStack() {
        return getChestGui().getInventoryComponent().getItem(getSlot() % 9, getSlot() / 9);
    }

    public void updateItemStack(ItemMeta meta) {
        getChestGui().getItems().stream()
                .filter(item -> item.equals(getGUIItem()))
                .forEach(guiItem -> guiItem.getItem().setItemMeta(meta));
        getChestGui().update();
    }

    public void replaceGUIItem(int slot, GuiItem target) {
        getPane().addItem(target, slot % 9, slot / 9);
        getChestGui().update();
    }

    public StaticPane getPane() {
        return (StaticPane) getChestGui().getPanes().iterator().next();
    }

}
