package ui.connector;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public interface ListInteraction<T> {
    void onSelected(T t);

    void onRemoved(int index);
}
