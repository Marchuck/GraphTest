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

    private List<AbstractNode> valueNodeList = new ArrayList<>();

    public PropertyNode addNode(AbstractNode node) {
        valueNodeList.add(node);
        return this;
    }

    @Override
    public AbstractNode sort() {
        Collections.sort(valueNodeList);
        return this;
    }

    public List<AbstractNode> getValueNodeList() {
        return valueNodeList;
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
    public float getEdgeLength() {
        return 3;
    }

    @Override
    public List<AbstractNode> getNodes() {
        return valueNodeList;
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

}
