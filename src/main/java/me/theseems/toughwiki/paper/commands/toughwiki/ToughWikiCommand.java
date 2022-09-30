package me.theseems.toughwiki.paper.commands.toughwiki;

import me.theseems.toughwiki.BuildConstants;
import me.theseems.toughwiki.paper.commands.CommandContainer;
import me.theseems.toughwiki.paper.commands.debug.WikiDebugSubCommandContainer;

public class ToughWikiCommand extends CommandContainer {
    public ToughWikiCommand() {
        add(new WikiShowPageSubCommand());
        add(new WikiReloadSubCommand());
        add(new WikiListPagesSubCommand());

        if (BuildConstants.DEV_BUILD) {
            add(new WikiDebugSubCommandContainer());
        }
    }
}
