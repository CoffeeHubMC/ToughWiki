package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.variety.BackAction;
import org.bukkit.entity.HumanEntity;

public class BackActionHandler extends IFWikiPageActionHandler<BackAction> {
    public BackActionHandler() {
        super(BackAction.class);
    }

    @Override
    protected void proceed(BackAction action, IFWikiActionSender sender) {
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
