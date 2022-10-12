package me.theseems.toughwiki.api.component;

public interface ComponentContainer {
    <T> Component<T> get(String name);

    <T> void store(String name, Component<T> component);

    default <T> T getValue(String name) {
        Component<T> result = get(name);
        if (result == null) {
            return null;
        }
        return result.getValue();
    }

    default <T> void storeValue(String name, T value) {
        store(name, new ValueComponent<>(value));
    }

    default <T> boolean exists(String name) {
        return this.<T>getValue(name) != null;
    }

    default <T1, T2> boolean exists(String first, String second) {
        return this.<T1>exists(first) && this.<T2>exists(second);
    }

    default <T1, T2, T3> boolean exists(String first, String second, String third) {
        return this.<T1, T2>exists(first, second) && this.<T3>exists(third);
    }

    default <T1, T2, T3, T4> boolean exists(String first, String second, String third, String fourth) {
        return this.<T1, T2, T3>exists(first, second, third) && this.<T4>exists(fourth);
    }
}
