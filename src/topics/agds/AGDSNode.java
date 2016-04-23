package topics.agds;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class AGDSNode implements AbstractNode {

    private String name;
    private List<AbstractNode> nodes = new LinkedList<>();
    private float value;
    private char type;

    public AGDSNode(char type) {
        this.type = type;
    }

    public AGDSNode withName(String name) {
        this.name = name;
        return this;
    }

    public AGDSNode withValue(float v) {
        this.value = v;
        return this;
    }

    public AGDSNode addNode(AbstractNode node) {
        this.nodes.add(node);
        return this;
    }

    public AGDSNode addNodes(AbstractNode... nodes) {
        Collections.addAll(this.nodes, nodes);
        return this;
    }

    @Override
    public AbstractNode sort() {
        Collections.sort(this.nodes);
        return this;
    }

    @Override
    public boolean typeOf(char type) {
        return type == this.type;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return Float.compare(this.getValue(), o.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AGDSNode agdsNode = (AGDSNode) o;

        if (Float.compare(agdsNode.value, value) != 0) return false;
        return name != null ? name.equals(agdsNode.name) : agdsNode.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != +0.0f ? Float.floatToIntBits(value) : 0);
        return result;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public List<AbstractNode> getNodes() {
        return nodes;
    }


}
