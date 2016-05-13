package topics.agds.nodes;


import agds.AGDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class PropertyNode extends AbstractNode implements Extremable {

    public PropertyNode(String name) {
        super(name);
    }

    private List<ValueNode> valueNodeList = new ArrayList<>();

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

    @Override
    public ValueNode getMinNode() {
        AbstractNode abstractNode = Collections.min(valueNodeList);
        return ((ValueNode) abstractNode);
    }

    @Override
    public ValueNode getMaxNode() {
        AbstractNode abstractNode = Collections.max(valueNodeList);
        return ((ValueNode) abstractNode);
    }

    @Override
    public AbstractNode getMeanNode() {
        Collections.sort(valueNodeList);
        return valueNodeList.get(valueNodeList.size() / 2);
    }

    public List<ValueNode> getNodes() {
        return valueNodeList;
    }

    public void calculateWeights(int foundIndex) {
        for (ValueNode valueNode : valueNodeList) {
            double wageValue = 1 - (Math.abs(valueNode.getValue()
                    - valueNodeList.get(foundIndex).getValue()))
                    / (getMaxNode().getValue() - getMinNode().getValue());
            valueNode.addCalculatedWeightToAllRecords(wageValue);
        }
    }

    public void clean() {
        for (ValueNode valueNode : valueNodeList) valueNode.clean();
    }
}
