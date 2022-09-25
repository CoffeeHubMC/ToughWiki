package me.theseems.toughwiki.paper.listener;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(ToughWiki.getPlugin(), () -> {
            for (WikiPageView allView : ToughWikiAPI.getInstance().getViewManager().getAllViews()) {
                allView.dispose(event.getPlayer().getUniqueId());
            }
        });
    }
}
