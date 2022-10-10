package me.theseems.toughwiki.paper.view.action.handlers;

import me.theseems.toughwiki.api.ToughWikiAPI;
import me.theseems.toughwiki.api.WikiPage;
import me.theseems.toughwiki.paper.view.action.BaseWikiPageActionHandler;
import me.theseems.toughwiki.paper.view.action.sender.InteractEventWikiActionSender;
import me.theseems.toughwiki.paper.view.action.variety.BackAction;
import org.bukkit.entity.HumanEntity;

public class BackActionHandler extends BaseWikiPageActionHandler<InteractEventWikiActionSender, BackAction> {
    public BackActionHandler() {
        super(InteractEventWikiActionSender.class, BackAction.class);
    }

    @Override
    protected void handle(BackAction action, InteractEventWikiActionSender sender) {
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
