package ui.connector;

import java.util.Collection;

/**
 * @author Lukasz
 * @since 27.05.2016.
 */
public interface ResultCallback<T> {
    void onComputed(Collection<T> result);
}
