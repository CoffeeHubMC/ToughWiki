package me.theseems.toughwiki.paper.item;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemTransformer {
    public abstract boolean supports(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config);

    public abstract void transform(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config);
}
