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
public class GenericRecordNode<T extends Comparable<T>> extends AbstractNode {

    public double getTotalWeight() {
        return totalWeight;
    }

    private double totalWeight;
    private GenericClassNode<T> classNode;

    public GenericClassNode<T> getClassNode() {
        return classNode;
    }

    private SortedList<GenericValueNode<T>> valueNodeList = new SortedList<>(new Comparator<GenericValueNode<T>>() {
        @Override
        public int compare(GenericValueNode<T> o1, GenericValueNode<T> o2) {
            return o1.compareTo(o2);
        }
    });

    public GenericRecordNode(String name) {
        super(name);
    }


    public GenericRecordNode<T> setClassNode(GenericClassNode<T> node) {
        this.classNode = node;
        return this;
    }

    public GenericRecordNode<T> addNode(GenericValueNode<T> node) {
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
    public List<GenericValueNode<T>> getNodes() {
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
        if (totalWeight < minWeight) minWeight = totalWeight;
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
        if (!(o instanceof GenericRecordNode)) return false;

        GenericRecordNode that = (GenericRecordNode) o;

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
