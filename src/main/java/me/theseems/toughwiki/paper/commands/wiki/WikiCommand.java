package me.theseems.toughwiki.paper.commands.wiki;

import me.theseems.toughwiki.BuildConstants;
import me.theseems.toughwiki.paper.commands.CommandContainer;

public class WikiCommand extends CommandContainer {
    public WikiCommand() {
        add(new WikiShowPageCommand());
        add(new WikiReloadCommand());
        add(new WikiListPagesCommand());

        if (BuildConstants.DEV_BUILD) {
            add(new WikiDebugCommand());
        }
    }
}
