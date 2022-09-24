package me.theseems.toughwiki.paper.item.initializer;

import io.lumine.mythic.api.MythicProvider;
import io.lumine.mythic.bukkit.adapters.BukkitItemStack;
import io.lumine.mythic.core.items.MythicItem;
import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.item.ItemInitializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MythicMobsItemInitializer extends ItemInitializer {
    @Override
    public boolean supports(Player player, WikiPageItemConfig config) {
        return config.getType().startsWith("mythic:");
    }

    @Override
    public ItemStack make(Player player, WikiPageItemConfig config) {
        String itemName = config.getType().replaceFirst("mythic:", "");

        MythicItem item = MythicProvider.get()
                .getItemManager()
                .getItem(itemName)
                .orElseThrow(() -> new IllegalStateException("Unknown mythic item: " + itemName));

        return ((BukkitItemStack) item.generateItemStack(1)).build();
    }
}
