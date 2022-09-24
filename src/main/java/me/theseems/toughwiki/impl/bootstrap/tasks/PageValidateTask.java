package me.theseems.toughwiki.impl.bootstrap.tasks;

import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.config.ToughWikiConfig;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class PageValidateTask extends BootstrapTask {
    private final Set<String> used;

    public PageValidateTask() {
        super("pageValidate", Phase.POST_CONFIG);
        this.used = new HashSet<>();
    }

    private boolean checkPage(ToughWikiConfig config, String name, String previous) {
        if (config.getParent() == null) {
            return true;
        }
        if (used.contains(name)) {
            throw new IllegalStateException(
                    "Incorrect configuration: page '%s' is used twice. Please, check your tree ('%s<-%s)"
                    .formatted(name, name, previous));
        }

        used.add(name);
        for (Map.Entry<String, ToughWikiConfig> child : config.getPages().entrySet()) {
            if (!checkPage(child.getValue(), child.getKey(), name)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void run(Logger logger) {
        checkPage(ToughWiki.getToughConfig(), "<root>", "<>");
    }
}
