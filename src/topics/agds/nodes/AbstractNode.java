package topics.agds.nodes;

import agds.DrawableNode;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public abstract class AbstractNode implements Comparable<AbstractNode>, DrawableNode {

    protected String name;

    public AbstractNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public abstract AbstractNode sort();

    public abstract AbstractNode addNode(AbstractNode node);

    public abstract List<AbstractNode> getNodes();
}
