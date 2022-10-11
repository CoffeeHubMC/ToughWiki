package me.theseems.toughwiki.impl.component;

import me.theseems.toughwiki.api.component.Component;
import me.theseems.toughwiki.api.component.ComponentContainer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleComponentContainer implements ComponentContainer {
    private final Map<String, Component<?>> componentMap;

    public SimpleComponentContainer() {
        componentMap = new ConcurrentHashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Component<T> get(String name) {
        Component<?> component = componentMap.get(name);
        if (component == null) {
            return null;
        }
        try {
            return (Component<T>) component;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public <T> void store(String name, Component<T> component) {
        if (componentMap.containsKey(name)) {
            throw new IllegalStateException("Component '" + name + "' already exists");
        }

        componentMap.put(name, component);
    }
}
