package me.theseems.toughwiki.paper.item;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemFactory {
    private final List<ItemInitializer> initializerList;
    private final List<ItemTransformer> transformerList;

    public ItemFactory() {
        this.initializerList = new ArrayList<>();
        this.transformerList = new ArrayList<>();
    }

    public void addInitializer(ItemInitializer initializer) {
        initializerList.add(initializer);
    }

    public void addTransformer(ItemTransformer transformer) {
        transformerList.add(transformer);
    }

    public ItemStack produce(Player player, WikiPageItemConfig itemConfig) {
        ItemStack stack = null;
        ItemInitializer initializer = null;
        for (ItemInitializer itemInitializer : initializerList) {
            if (itemInitializer.supports(player, itemConfig)) {
                stack = itemInitializer.make(player, itemConfig);
                initializer = itemInitializer;
                break;
            }
        }

        if (initializer == null) {
            throw new IllegalStateException("Failed to produce item. No initializer found for config: " + itemConfig);
        }

        for (ItemTransformer itemTransformer : transformerList) {
            if (itemTransformer.supports(player, initializer, stack, itemConfig)) {
                itemTransformer.transform(player, initializer, stack, itemConfig);
            }
        }

        return stack;
    }
}
