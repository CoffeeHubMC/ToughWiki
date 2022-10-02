package me.theseems.toughwiki.paper.task;

import me.theseems.toughwiki.impl.SimpleWikiPage;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.ToughWiki;
import me.theseems.toughwiki.api.ToughWikiAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PageRegisterTask extends BootstrapTask {
    public PageRegisterTask() {
        super("pageRegister", Phase.POST_CONFIG);
    }

    @Override
    public void run(Logger logger) {
        Map<String, SimpleWikiPage> memory = new HashMap<>();
        ToughWiki.getToughConfig()
                .getPages()
                .forEach((name, config) -> memory.put(name,
                        new SimpleWikiPage(name, null, new ArrayList<>(), config.getContent())));

        ToughWiki.getToughConfig().getPages().forEach((name, config) -> {
            if (config.getParent() == null) {
                return;
            }
            if (!memory.containsKey(config.getParent())) {
                throw new IllegalStateException("Parent not found for '%s': %s"
                        .formatted(name, config.getParent()));
            }

            SimpleWikiPage parent = memory.get(config.getParent());
            SimpleWikiPage child = memory.get(name);

            parent.getChildren().add(child);
            child.setParent(parent);
        });

        memory.forEach((s, simpleWikiPage) ->
                ToughWikiAPI.getInstance().getPageRepository().store(simpleWikiPage));

        logger.info("Registered: " + memory.size() + " page(s)");
    }
}
