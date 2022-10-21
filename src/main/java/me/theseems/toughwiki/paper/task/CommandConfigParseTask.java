package me.theseems.toughwiki.paper.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import me.theseems.toughwiki.config.CustomCommandConfig;
import me.theseems.toughwiki.impl.bootstrap.BootstrapTask;
import me.theseems.toughwiki.impl.bootstrap.Phase;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class CommandConfigParseTask extends BootstrapTask {
    private final File configFile;
    private final Consumer<CustomCommandConfig> consumer;

    public CommandConfigParseTask(@NotNull File configFile, @NotNull Consumer<CustomCommandConfig> consumer) {
        super("parseCommandConfig", Phase.CONFIG);
        this.configFile = configFile;
        this.consumer = consumer;
    }

    @Override
    public void run(Logger logger) throws Exception {
        if (!configFile.getParentFile().exists()) {
            configFile.mkdir();
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        if (!configFile.exists()) {
            configFile.createNewFile();
            mapper.writeValue(configFile, new CustomCommandConfig());
        }

        try {
            consumer.accept(mapper.readValue(configFile, CustomCommandConfig.class));
        } catch (MismatchedInputException e) {
            consumer.accept(new CustomCommandConfig());
        }
    }
}
