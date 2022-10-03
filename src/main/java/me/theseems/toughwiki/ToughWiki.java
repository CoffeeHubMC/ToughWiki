package me.theseems.toughwiki;

import me.theseems.toughwiki.config.ToughWikiConfig;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.impl.bootstrap.ToughWikiBootstrap;
import me.theseems.toughwiki.impl.bootstrap.tasks.ApiInitializationTask;
import me.theseems.toughwiki.impl.bootstrap.tasks.PageClearTask;
import me.theseems.toughwiki.impl.bootstrap.tasks.PageViewDisposeTask;
import me.theseems.toughwiki.paper.item.ItemFactory;
import me.theseems.toughwiki.paper.item.ItemFactoryInitializationTask;
import me.theseems.toughwiki.paper.task.*;
import me.theseems.toughwiki.paper.view.action.IFWikiActionHandlerRegisterTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.logging.Logger;

public final class ToughWiki extends JavaPlugin {
    private static ToughWikiConfig config;
    private static ItemFactory itemFactory;
    private static ToughWiki plugin;
    private static ToughWikiBootstrap bootstrap;

    public static ToughWikiConfig getToughConfig() {
        return config;
    }

    public static ItemFactory getItemFactory() {
        return itemFactory;
    }

    private void setItemFactory(ItemFactory itemFactory) {
        ToughWiki.itemFactory = itemFactory;
    }

    public static @NotNull Logger getPluginLogger() {
        return plugin.getLogger();
    }

    public static ToughWikiBootstrap getBootstrap() {
        return bootstrap;
    }

    public static ToughWiki getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        ToughWiki.plugin = this;
        ToughWiki.bootstrap = new ToughWikiBootstrap(getLogger());

        bootstrap.add(new ApiInitializationTask());
        bootstrap.add(new IFWikiActionHandlerRegisterTask());
        bootstrap.add(new ItemFactoryInitializationTask(this::setItemFactory));
        bootstrap.add(new ConfigParseTask(new File(getDataFolder(), "config.yml"), this::setConfig));
        bootstrap.add(new PageValidateTask());
        bootstrap.add(new PageRegisterTask());
        bootstrap.add(new PaperRegisterViewTask());

        bootstrap.add(new PaperCommandRegisterTask(plugin));
        bootstrap.add(new PaperListenerRegisterTask(plugin));

        bootstrap.add(new PaperListenerUnregisterTask());
        bootstrap.add(new PageViewDisposeTask());
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
}
