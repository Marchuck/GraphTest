package topics.agds.nodes;


import agds.AGDS;
import common.SortedList;

import java.util.Comparator;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class GenericPropertyNode<T extends Comparable<T>> extends AbstractNode {

    public GenericPropertyNode(String name) {
        super(name);
    }

    private SortedList<GenericValueNode<T>> valueNodeList = new SortedList<>(new Comparator<GenericValueNode<T>>() {
        @Override
        public int compare(GenericValueNode<T> o1, GenericValueNode<T> o2) {
            return o1.compareTo(o2);
        }
    });


    public GenericPropertyNode<T> addNode(GenericValueNode<T> node) {
        valueNodeList.add(node);
        return this;
    }

    public GenericPropertyNode addValueNode(GenericValueNode<T> valueNode) {
        this.valueNodeList.add(valueNode);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.PROPERTY_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDS.PROPERTY_NODE_WEIGHT;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }

    public GenericValueNode<T> getMinNode() {
        //AbstractNode abstractNode = Collections.min(valueNodeList);
        return valueNodeList.get(0);
    }

    public GenericValueNode<T> getMaxNode() {
//        AbstractNode abstractNode = Collections.max(valueNodeList);
        return valueNodeList.get(valueNodeList.size() - 1);
    }

    public List<GenericValueNode<T>> getNodes() {
        return valueNodeList;
    }

    public void calculateWeights(int foundIndex) {
        for (GenericValueNode<T> valueNode : valueNodeList) {
            double weightValue = valueNode.calculateWeight(valueNode, valueNodeList.get(foundIndex), getMaxNode(),
                    getMinNode());
//            double weightValue = 1 - (Math.abs(valueNode.getValue()
//                    - valueNodeList.get(foundIndex).getValue()))
//                    / (getMaxNode().getValue() - getMinNode().getValue());
            valueNode.addCalculatedWeightToAllRecords(weightValue);
        }
    }

    public void clean() {
        for (GenericValueNode<T> valueNode : valueNodeList) valueNode.clean();
    }
}
