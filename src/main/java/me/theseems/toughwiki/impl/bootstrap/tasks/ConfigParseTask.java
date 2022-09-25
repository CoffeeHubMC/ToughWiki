package me.theseems.toughwiki.impl.bootstrap.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.config.FlatToughWikiConfig;
import me.theseems.toughwiki.config.ToughWikiConfig;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ConfigParseTask extends BootstrapTask {
    private final File configFile;
    private final Consumer<ToughWikiConfig> consumer;

    public ConfigParseTask(@NotNull File configFile, @NotNull Consumer<ToughWikiConfig> consumer) {
        super("parseConfig", Phase.CONFIG);
        this.configFile = configFile;
        this.consumer = consumer;
    }

    @Override
    public void run(Logger logger) throws Exception {
        if (!configFile.getParentFile().exists()) {
            configFile.mkdir();
        }
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ToughWikiConfig wikiConfig = mapper.readValue(configFile, ToughWikiConfig.class);

        File pagesFolder = new File(configFile.getParentFile(), "pages");
        File[] additionalPages = pagesFolder.listFiles();
        if (pagesFolder.exists() && additionalPages != null) {
            for (File file : additionalPages) {
                if (file.getName().endsWith(".yml")) {
                    FlatToughWikiConfig flatToughWikiConfig = mapper.readValue(file, FlatToughWikiConfig.class);
                    flatToughWikiConfig.getPages().forEach((s, config) -> {
                        if (wikiConfig.getPages().containsKey(s)) {
                            throw new IllegalStateException(
                                    "File " + file + " contains a page that is already included in the main config");
                        }

                        logger.info("Imported " + s + " (" + config + ")");
                        wikiConfig.getPages().put(s, config);
                    });

                    logger.info("Imported %d page(s) from %s"
                            .formatted(flatToughWikiConfig.getPages().size(), file.getName()));
                }
            }
        }

        consumer.accept(wikiConfig);
    }
}
