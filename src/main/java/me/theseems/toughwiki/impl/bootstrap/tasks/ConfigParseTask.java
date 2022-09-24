package me.theseems.toughwiki.impl.bootstrap.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

        consumer.accept(new ObjectMapper(new YAMLFactory()).readValue(configFile, ToughWikiConfig.class));
    }
}
