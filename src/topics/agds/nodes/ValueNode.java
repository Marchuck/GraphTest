package topics.agds.nodes;

import agds_core.AGDSConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */


public class ValueNode extends AbstractNode {

    public double weight;
    private double value;
    private List<RecordNode> recordNodeList = new ArrayList<>();

    private PropertyNode propertyNode;

    public ValueNode(String name) {
        super(name);
    }

    public ValueNode(double value) {
        super(AbstractNode.NOT_CLASSIFIED);
        this.value = value;
    }

    public ValueNode addNode(RecordNode node) {
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
    public String getStyleSheet() {
        return AGDSConstants.VALUE_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDSConstants.VALUE_NODE_WEIGHT;
    }

    // @Override
    public List<RecordNode> getNodes() {
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

    public void addCalculatedWeightToAllRecords(double weight) {
        for (RecordNode rNode : recordNodeList) {
            rNode.addToTotalWage(weight);
        }
    }

    public void clean() {
        weight = 0f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueNode)) return false;

        ValueNode valueNode = (ValueNode) o;

        return Double.compare(valueNode.value, value) == 0;

    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
