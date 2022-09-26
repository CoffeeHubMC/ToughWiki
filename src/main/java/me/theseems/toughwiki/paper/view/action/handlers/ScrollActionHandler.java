package me.theseems.toughwiki.paper.view.action.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.view.Action;
import me.theseems.toughwiki.paper.view.action.IFWikiActionSender;
import me.theseems.toughwiki.paper.view.action.IFWikiPageActionHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScrollActionHandler extends IFWikiPageActionHandler {
    @Override
    public boolean supports(Action action, IFWikiActionSender sender) {
        return action == Action.SCROLL_ITEM;
    }

    @Override
    protected void proceed(Action action, IFWikiActionSender sender) {
        if (!(sender.getEvent().getWhoClicked() instanceof Player)) {
            return;
        }

        int maxSize = sender.getView().getMaxLines();
        Player player = ((Player) sender.getEvent().getWhoClicked()).getPlayer();
        List<Component> componentList = ToughWiki.getItemFactory().produce(player, sender.getItemConfig()).lore();
        if (componentList == null || componentList.size() <= maxSize) {
            return;
        }

        ObjectNode context = sender.getContext();
        int currentLinesOffset = Optional.ofNullable(context.get("scroll-offset-item-" + sender.getSlot()))
                .filter(JsonNode::isInt)
                .map(JsonNode::asInt)
                .orElse(1);

        if (currentLinesOffset >= componentList.size() - maxSize) {
            context.put("scroll-offset-item-" + sender.getSlot(), 0);
        } else {
            context.put("scroll-offset-item-" + sender.getSlot(), currentLinesOffset + 1);
        }

        ItemStack stack = sender.getItemStack();
        ItemMeta meta = stack.getItemMeta();

        Stream<Component> componentStream = componentList.stream()
                .skip(currentLinesOffset)
                .limit(maxSize);

        if (sender.getView().getSeeMore() != null) {
            componentStream = Stream.concat(componentStream, sender.getView().getSeeMore().stream());
        }

        meta.lore(componentStream.collect(Collectors.toList()));

        sender.updateItemStack(meta);
    }
}
