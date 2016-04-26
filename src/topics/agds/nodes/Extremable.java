package topics.agds.nodes;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public interface Extremable {

    AbstractNode getMinNode();

    AbstractNode getMaxNode();

    AbstractNode getMeanNode();

}
