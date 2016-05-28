package ui.connector;

import java.util.List;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public interface ResultCallback<T> {
    void onComputed(List<T> result);
}
