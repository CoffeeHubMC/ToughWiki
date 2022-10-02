package me.theseems.toughwiki.paper.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import me.theseems.toughwiki.config.FlatToughWikiConfig;
import me.theseems.toughwiki.config.ToughWikiConfig;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
        if (!pagesFolder.exists()) {
            pagesFolder.mkdir();
        }

        try (Stream<Path> stream = Files.walk(pagesFolder.toPath())) {
            stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".yml"))
                    .forEach(path -> {
                        File file = path.toFile();
                        FlatToughWikiConfig flatToughWikiConfig;
                        try {
                            flatToughWikiConfig = mapper.readValue(file, FlatToughWikiConfig.class);
                        } catch (IOException e) {
                            logger.warning("Failed to load config @ " + path + ":");
                            e.printStackTrace();
                            return;
                        }
                        flatToughWikiConfig.getPages().forEach((name, config) -> {
                            if (wikiConfig.getPages().containsKey(name)) {
                                logger.warning("File %s contains a page that is present at least twice: '%s'"
                                        .formatted(file, name));
                                return;
                            }

                            wikiConfig.getPages().put(name, config);
                            logger.info("Loaded page %s from %s".formatted(name, path));
                        });
                    });
        }

        consumer.accept(wikiConfig);
    }
}
