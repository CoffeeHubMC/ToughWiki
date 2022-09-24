package me.theseems.toughwiki.paper.item;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ItemInitializer {
    public abstract boolean supports(Player player, WikiPageItemConfig config);

    public abstract ItemStack make(Player player, WikiPageItemConfig config);
}
