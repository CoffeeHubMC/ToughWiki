package me.theseems.toughwiki.api.component;

public interface Component<T> {
    T getValue();
    void setValue(T value);
}
