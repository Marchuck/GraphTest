package topics.agds.nodes;


import agds.AGDS;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class RecordNode extends AbstractNode {

    public double getTotalWeight() {
        return totalWeight;
    }

    private double totalWeight;
    private ClassNode classNode;

    public ClassNode getClassNode() {
        return classNode;
    }

    private List<ValueNode> valueNodeList = new ArrayList<>();

    public RecordNode(String name) {
        super(name);
    }


    public RecordNode setClassNode(ClassNode node) {
        this.classNode = node;
        return this;
    }

    public RecordNode addNode(ValueNode node) {
        valueNodeList.add(node);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.RECORD_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDS.RECORD_NODE_WEIGHT;
    }

    //  @Override
    public List<ValueNode> getNodes() {
        return valueNodeList;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }

    public void addToTotalWage(double weight) {
        totalWeight += weight;
    }

    public void clean() {
        totalWeight = 0f;
    }

    public boolean banned;
    public int weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecordNode)) return false;

        RecordNode that = (RecordNode) o;

        if (!classNode.equals(that.classNode)) return false;
        return valueNodeList.equals(that.valueNodeList);

    }

    @Override
    public int hashCode() {
        int result = classNode.hashCode();
        result = 31 * result + valueNodeList.hashCode();
        return result;
    }
}
