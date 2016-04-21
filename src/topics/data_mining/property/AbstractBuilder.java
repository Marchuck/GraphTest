package topics.data_mining.property;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 21.04.16.
 */
public interface AbstractBuilder {
    AbstractBuilder compute();

    List<Float> get();

    List<Float> getNormalized();

}
