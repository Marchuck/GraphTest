package topics.agds.nodes;

import agds.AGDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class ValueNode extends AbstractNode {

    private double value;
    private double wage;
    private List<AbstractNode> recordNodeList = new ArrayList<>();
    private PropertyNode propertyNode;

    public ValueNode(String name) {
        super(name);
    }

    public ValueNode addNode(AbstractNode node) {
        recordNodeList.add(node);
        return this;
    }

    public ValueNode withPropertyNode(PropertyNode propertyNode) {
        this.propertyNode = propertyNode;
        return this;
    }

    public ValueNode withValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public AbstractNode sort() {
        Collections.sort(recordNodeList);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.VALUE_NODE_STYLESHEET;
    }

    @Override
    public float getEdgeLength() {
        return 0.5f;
    }

    @Override
    public List<AbstractNode> getNodes() {
        return recordNodeList;
    }

    @Override
    public int compareTo(AbstractNode o) {
        if (o instanceof ValueNode) return Double.compare(value, ((ValueNode) o).value);
        return 0;
    }

    public double getValue() {
        return value;
    }
}
