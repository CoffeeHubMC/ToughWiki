package me.theseems.toughwiki.impl.bootstrap;

import java.util.logging.Logger;

public abstract class BootstrapTask {
    private final String name;
    private final Phase phase;
    private final boolean optional;

    public BootstrapTask(String name, Phase phase) {
        this.name = name;
        this.phase = phase;
        this.optional = false;
    }

    public BootstrapTask(String name, Phase phase, boolean optional) {
        this.name = name;
        this.phase = phase;
        this.optional = optional;
    }

    public Phase getPhase() {
        return phase;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public abstract void run(Logger logger) throws Exception;
}
