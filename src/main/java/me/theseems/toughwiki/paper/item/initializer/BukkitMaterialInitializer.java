package me.theseems.toughwiki.paper.item.initializer;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.item.ItemInitializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class BukkitMaterialInitializer extends ItemInitializer {
    @Override
    public boolean supports(Player player, WikiPageItemConfig config) {
        return Material.getMaterial(config.getType()) != null;
    }

    @Override
    public ItemStack make(Player player, WikiPageItemConfig config) {
        return new ItemStack(Objects.requireNonNull(Material.getMaterial(config.getType())));
    }
}
