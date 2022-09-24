package me.theseems.toughwiki.impl.bootstrap;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ToughWikiBootstrap {
    private final List<BootstrapTask> bootstrapTasks;
    private final Logger logger;

    public ToughWikiBootstrap(Logger logger) {
        this.bootstrapTasks = new ArrayList<>();
        this.logger = Logger.getLogger(logger.getName() + ":Bootstrap");
    }

    public void add(@NotNull BootstrapTask bootstrapTask) {
        bootstrapTasks.add(bootstrapTask);
    }

    public void replace(String taskName, BootstrapTask task) {
        for (int i = 0; i < bootstrapTasks.size(); i++) {
            if (bootstrapTasks.get(i).getName().equals(taskName)) {
                bootstrapTasks.remove(bootstrapTasks.get(i));
                bootstrapTasks.add(i, task);
                return;
            }
        }
    }

    public boolean execute(@NotNull Phase phase) {
        for (BootstrapTask bootstrapTask : bootstrapTasks) {
            if (bootstrapTask.getPhase() == phase) {
                Logger taskLogger = Logger.getLogger(logger.getName() + "::" + bootstrapTask.getName());
                try {
                    bootstrapTask.run(taskLogger);
                } catch (Exception e) {
                    taskLogger.severe("Error executing task: " + e.getMessage());
                    if (!bootstrapTask.isOptional()) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean execute(@NotNull Phase... phases) {
        for (Phase phase : phases) {
            if (!execute(phase)) {
                return false;
            }
        }

        return true;
    }

    public List<BootstrapTask> getBootstrapTasks() {
        return bootstrapTasks;
    }
}
