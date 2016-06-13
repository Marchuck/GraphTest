package topics.agds.engine;

/**
 * @author Lukasz
 * @since 11.06.2016.
 */
public interface Cellable<T> extends Comparable<T>{
    String getName();
    T[] getValues();

}
