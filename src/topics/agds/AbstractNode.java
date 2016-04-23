package topics.agds;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public interface AbstractNode  extends Comparable<AbstractNode>{
    String getName();
    float getValue();
    List<AbstractNode> getNodes();
    AbstractNode sort();

    boolean typeOf(char type);
}
