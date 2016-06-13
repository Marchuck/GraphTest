package topics.agds.nodes;

import agds_core.AGDSConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */


public class GenericValueNode<T extends Comparable<T>> extends AbstractNode {

    public double weight;
    private T value;
    private List<GenericRecordNode<T>> recordNodeList = new ArrayList<>();

    private PropertyNode propertyNode;

    public GenericValueNode(String name) {
        super(name);
    }

    public GenericValueNode(T value) {
        super(AbstractNode.NOT_CLASSIFIED);
        this.value = value;
    }

    public GenericValueNode<T> addNode(GenericRecordNode<T> node) {
        recordNodeList.add(node);
        return this;
    }

    public GenericValueNode<T> withPropertyNode(PropertyNode propertyNode) {
        this.propertyNode = propertyNode;
        return this;
    }

    public GenericValueNode<T> withValue(T value) {
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
    public List<GenericRecordNode<T>> getNodes() {
        return recordNodeList;
    }

    @Override
    public int compareTo(AbstractNode o) {
        if (o instanceof GenericValueNode)
            return value.compareTo(((GenericValueNode<T>) o).value);//Double.compare(value, ((GenericValueNode) o).value);
        return 0;
    }

    public T getValue() {
        return value;
    }

    public void addCalculatedWeightToAllRecords(double weight) {
        for (GenericRecordNode<T> rNode : recordNodeList) {
            rNode.addToTotalWage(weight);
        }
    }

    public void clean() {
        weight = 0f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericValueNode)) return false;

        GenericValueNode valueNode = (GenericValueNode) o;

//        return Double.compare(valueNode.value, value) == 0;
        return valueNode.compareTo((AbstractNode) value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value.hashCode());
        return (int) (temp ^ (temp >>> 32));
    }

    public double calculateWeight(GenericValueNode<T> valueNode,
                                  GenericValueNode<T> tGenericValueNode,
                                  GenericValueNode<T> maxNode, GenericValueNode<T> minNode) {
        return 0;
    }
}
