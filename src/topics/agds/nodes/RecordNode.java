package topics.agds.nodes;


import agds.AGDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class RecordNode extends AbstractNode {

    private double totalWeight;
    private ClassNode classNode;
    private List<AbstractNode> valueNodeList = new ArrayList<>();

    public RecordNode(String name) {
        super(name);
    }

    public RecordNode addNodes(AbstractNode... nodes) {
        Collections.addAll(valueNodeList, nodes);
        return this;
    }

    public RecordNode setClassNode(ClassNode node) {
        this.classNode = node;
        return this;
    }

    public RecordNode addNode(AbstractNode node) {
        valueNodeList.add(node);
        return this;
    }

    @Override
    public AbstractNode sort() {
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.RECORD_NODE_STYLESHEET;
    }

    @Override
    public float getEdgeLength() {
        return 0.5f;
    }

    @Override
    public List<AbstractNode> getNodes() {
        return valueNodeList;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }
}
