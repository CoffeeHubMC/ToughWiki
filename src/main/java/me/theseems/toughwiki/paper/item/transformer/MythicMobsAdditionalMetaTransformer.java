package me.theseems.toughwiki.paper.item.transformer;

import me.theseems.toughwiki.api.WikiPageItemConfig;
import me.theseems.toughwiki.paper.item.ItemInitializer;
import me.theseems.toughwiki.paper.item.ItemTransformer;
import me.theseems.toughwiki.paper.item.initializer.MythicMobsItemInitializer;
import me.theseems.toughwiki.utils.TextUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MythicMobsAdditionalMetaTransformer extends ItemTransformer {
    @Override
    public boolean supports(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        return initializer instanceof MythicMobsItemInitializer;
    }

    @Override
    public void transform(Player player, ItemInitializer initializer, ItemStack stack, WikiPageItemConfig config) {
        if (config.getLore() != null) {
            ItemMeta meta = stack.getItemMeta();
            meta.lore(Stream.concat(
                    Optional.ofNullable(meta.lore()).orElse(List.of()).stream(),
                    Stream.concat(
                            Stream.of(Component.empty()),
                            config.getLore().stream().map(TextUtils::parse))
            ).collect(Collectors.toList()));
            stack.setItemMeta(meta);
        }
    }
}
