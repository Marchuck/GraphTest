package topics.agds.nodes;

import agds.AGDS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class ValueNode extends AbstractNode {

    private double value;
    public double weight;
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
        return AGDS.VALUE_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDS.VALUE_NODE_WEIGHT;
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

}
