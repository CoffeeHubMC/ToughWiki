package me.theseems.toughwiki.paper.item.transformer;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.item.ItemInitializer;
import me.theseems.toughwiki.paper.item.ItemTransformer;
import me.theseems.toughwiki.paper.item.initializer.BukkitMaterialInitializer;
import me.theseems.toughwiki.utils.TextUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.stream.Collectors;

public class BukkitMetaFillTransformer extends ItemTransformer {
    @Override
    public boolean supports(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        return initializer instanceof BukkitMaterialInitializer;
    }

    @Override
    public void transform(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        ItemMeta meta = stack.getItemMeta();
        if (config.getTitle() != null) {
            meta.displayName(TextUtils.parse(config.getTitle()));
        }
        if (config.getLore() != null) {
            meta.lore(config.getLore().stream().map(TextUtils::parse).collect(Collectors.toList()));
        }
        stack.setItemMeta(meta);
    }
}
