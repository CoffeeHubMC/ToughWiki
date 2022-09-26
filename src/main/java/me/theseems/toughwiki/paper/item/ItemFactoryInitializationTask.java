package me.theseems.toughwiki.paper.item;

import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.item.initializer.BukkitMaterialInitializer;
import me.theseems.toughwiki.paper.item.initializer.MythicMobsItemInitializer;
import me.theseems.toughwiki.paper.item.transformer.BukkitMetaFillTransformer;
import me.theseems.toughwiki.paper.item.transformer.CustomModelDataTransformer;
import me.theseems.toughwiki.paper.item.transformer.MythicMobsAdditionalMetaTransformer;
import me.theseems.toughwiki.paper.item.transformer.PlaceholderApiTransformer;
import org.bukkit.Bukkit;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class ItemFactoryInitializationTask extends BootstrapTask {
    private final Consumer<ItemFactory> itemFactoryConsumer;

    public ItemFactoryInitializationTask(Consumer<ItemFactory> consumer) {
        super("itemFactoryInit", Phase.PRE_CONFIG);
        this.itemFactoryConsumer = consumer;
    }

    @Override
    public void run(Logger logger) {
        ItemFactory itemFactory = new ItemFactory();
        itemFactory.addInitializer(new BukkitMaterialInitializer());
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            itemFactory.addInitializer(new MythicMobsItemInitializer());
        }

        itemFactory.addTransformer(new BukkitMetaFillTransformer());
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            itemFactory.addTransformer(new MythicMobsAdditionalMetaTransformer());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            itemFactory.addTransformer(new PlaceholderApiTransformer());
        }
        itemFactory.addTransformer(new CustomModelDataTransformer());

        itemFactoryConsumer.accept(itemFactory);
    }
}
