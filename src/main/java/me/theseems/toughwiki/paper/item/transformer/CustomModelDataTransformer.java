package me.theseems.toughwiki.paper.item.transformer;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.item.ItemInitializer;
import me.theseems.toughwiki.paper.item.ItemTransformer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomModelDataTransformer extends ItemTransformer {
    @Override
    public boolean supports(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        return true;
    }

    @Override
    public void transform(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        if (config.getModifiers() != null
                && config.getModifiers().containsKey("customModelData")
                && config.getModifiers().get("customModelData") instanceof Number) {
            ItemMeta meta = stack.getItemMeta();
            meta.setCustomModelData(((Number) config.getModifiers().get("customModelData")).intValue());
            stack.setItemMeta(meta);
        }
    }
}
