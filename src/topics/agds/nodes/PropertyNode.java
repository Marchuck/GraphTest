package topics.agds.nodes;


import agds_core.AGDSConstants;
import common.SortedList;

import java.util.Comparator;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class PropertyNode extends AbstractNode {

    private SortedList<ValueNode> valueNodeList = new SortedList<>(new Comparator<ValueNode>() {
        @Override
        public int compare(ValueNode o1, ValueNode o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
    });

    public PropertyNode(String name) {
        super(name);
    }

    public PropertyNode addNode(ValueNode node) {
        valueNodeList.add(node);
        return this;
    }

    public PropertyNode addValueNode(ValueNode valueNode) {
        this.valueNodeList.add(valueNode);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDSConstants.PROPERTY_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDSConstants.PROPERTY_NODE_WEIGHT;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }

    public ValueNode getMinNode() {
        //AbstractNode abstractNode = Collections.min(valueNodeList);
        return valueNodeList.get(0);
    }

    public ValueNode getMaxNode() {
//        AbstractNode abstractNode = Collections.max(valueNodeList);
        return valueNodeList.get(valueNodeList.size() - 1);
    }

    public List<ValueNode> getNodes() {
        return valueNodeList;
    }

    public void calculateWeights(int foundIndex) {
        for (ValueNode valueNode : valueNodeList) {
            double weightValue = 1 - (Math.abs(valueNode.getValue()
                    - valueNodeList.get(foundIndex).getValue()))
                    / (getMaxNode().getValue() - getMinNode().getValue());
            valueNode.addCalculatedWeightToAllRecords(weightValue);
        }
    }

    public void clean() {
        for (ValueNode valueNode : valueNodeList) valueNode.clean();
    }
}
