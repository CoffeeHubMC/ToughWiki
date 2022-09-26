package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import org.bukkit.entity.HumanEntity;

public class BackActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.BACK;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        WikiPage page = sender.getView().getPage();
        HumanEntity opener = sender.getEvent().getWhoClicked();

        if (page.getParent().isPresent()) {
            try {
                ToughWikiAPI.getInstance().getViewManager()
                        .getView(page.getParent().get())
                        .orElseThrow(() -> new IllegalStateException(
                                "No parent view found for player '%s' and page '%s'"
                                        .formatted(opener.getName(), page.getName())))
                        .show(opener.getUniqueId());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            opener.closeInventory();
        }
    }
}
