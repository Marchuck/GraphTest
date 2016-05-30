package topics.agds.nodes;


import agds.AGDS;
import common.SortedList;
import common.Utils;

import java.util.Comparator;
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

    private SortedList<ValueNode> valueNodeList = new SortedList<>(new Comparator<ValueNode>() {
        @Override
        public int compare(ValueNode o1, ValueNode o2) {
            return Double.compare(o1.getValue(), o2.getValue());
        }
    });

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
        Utils.log("Total weight : " + String.format("%.4f", totalWeight));
        if (totalWeight > maxWeight) maxWeight = totalWeight;
        if (totalWeight < maxWeight) minWeight = totalWeight;
    }

    public void clean() {
        totalWeight = maxWeight = minWeight = 0d;
    }

    public static double maxWeight = 0, minWeight = 0;
    public boolean banned;
    public int weight;
    public boolean visited;

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
