package me.theseems.toughwiki.api.component;

public class ValueComponent<T> implements Component<T> {
    private T value;

    public ValueComponent(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
