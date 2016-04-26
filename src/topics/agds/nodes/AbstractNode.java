package topics.agds.nodes;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public abstract class AbstractNode implements Comparable<AbstractNode> {

    protected String name;

    public AbstractNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract AbstractNode sort();

    public abstract String getStyleSheet();

    public abstract float getEdgeLength();
    public abstract AbstractNode addNode(AbstractNode node);

    public abstract List<AbstractNode> getNodes();
}
