package me.theseems.toughwiki;

import me.theseems.toughwiki.config.ToughWikiConfig;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.impl.bootstrap.ToughWikiBootstrap;
import me.theseems.toughwiki.impl.bootstrap.tasks.*;
import me.theseems.toughwiki.impl.bootstrap.tasks.itemfactory.ItemFactoryInitializationTask;
import me.theseems.toughwiki.paper.item.ItemFactory;
import me.theseems.toughwiki.paper.task.PaperCommandRegisterTask;
import me.theseems.toughwiki.paper.task.PaperRegisterViewTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Logger;

public final class ToughWiki extends JavaPlugin {
    private static ToughWikiConfig config;
    private static ItemFactory itemFactory;
    private static ToughWiki plugin;
    private static ToughWikiBootstrap bootstrap;

    @Override
    public void onEnable() {
        ToughWiki.plugin = this;
        ToughWiki.bootstrap = new ToughWikiBootstrap(getLogger());

        bootstrap.add(new ApiInitializationTask());
        bootstrap.add(new ItemFactoryInitializationTask(this::setItemFactory));
        bootstrap.add(new ConfigParseTask(new File(getDataFolder(), "config.yml"), this::setConfig));
        bootstrap.add(new PageValidateTask());
        bootstrap.add(new PageRegisterTask());
        bootstrap.add(new PaperRegisterViewTask());
        bootstrap.add(new PaperCommandRegisterTask(plugin));

        bootstrap.add(new PageClearTask());

        bootstrap.execute(Phase.PRE_CONFIG);
        bootstrap.execute(Phase.CONFIG);
        bootstrap.execute(Phase.POST_CONFIG);
    }

    @Override
    public void onDisable() {
        bootstrap.execute(Phase.SHUTDOWN);
    }

    private void setConfig(ToughWikiConfig config) {
        ToughWiki.config = config;
    }

    private void setItemFactory(ItemFactory itemFactory) {
        ToughWiki.itemFactory = itemFactory;
    }

    public static ToughWikiConfig getToughConfig() {
        return config;
    }

    public static ItemFactory getItemFactory() {
        return itemFactory;
    }

    public static @NotNull Logger getPluginLogger() {
        return plugin.getLogger();
    }

    public static ToughWikiBootstrap getBootstrap() {
        return bootstrap;
    }
}
