package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.paper.listener.PlayerLeaveListener;
import me.theseems.toughwiki.paper.listener.TabCompleteListener;

import java.util.logging.Logger;

public class PaperListenerRegisterTask extends BootstrapTask {
    private final ToughWiki toughWiki;
    public static TabCompleteListener tabCompleteListener;
    public static PlayerLeaveListener playerLeaveListener;

    public PaperListenerRegisterTask(ToughWiki toughWiki) {
        super("paperCommandRegister", Phase.POST_CONFIG);
        this.toughWiki = toughWiki;
    }

    @Override
    public void run(Logger logger) {
        tabCompleteListener = new TabCompleteListener();
        playerLeaveListener = new PlayerLeaveListener();

        toughWiki.getServer().getPluginManager().registerEvents(tabCompleteListener, toughWiki);
        toughWiki.getServer().getPluginManager().registerEvents(playerLeaveListener, toughWiki);
    }
}
