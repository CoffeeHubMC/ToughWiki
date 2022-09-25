package me.theseems.toughwiki.paper.listener;

import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.view.WikiPageView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements Listener {
    @EventHandler
    public void onTabCompleteForWikiDefaultCommand(AsyncTabCompleteEvent e) {
        if (e.isCancelled() || !e.isCommand() || e.isHandled()) {
            return;
        }

        if (!e.getBuffer().startsWith("/wiki") || e.getBuffer().split(" ").length > 1) {
            return;
        }

        List<String> list = new ArrayList<>();
        for (WikiPageView view : ToughWikiAPI.getInstance().getViewManager().getAllViews()) {
            if (e.getSender().hasPermission("toughwiki.command.showpage." + view.getPage().getName())) {
                list.add(view.getPage().getName());
            }
        }

        e.setCompletions(list);
    }
}
