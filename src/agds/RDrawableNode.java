package agds;

import java.util.ArrayList;
import java.util.List;

public class RDrawableNode implements DrawableNode, Comparable<RDrawableNode>, Resetable {
    @Override
    public int getEdgeWeight() {
        return AGDS.RECORD_NODE_WEIGHT;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.RECORD_NODE_STYLESHEET;
    }

    private String name;
    private Double totalWeight;
    private ClassDrawableNode classNode;
    private List<ValueDrawableNode> valueNodeList;

    /**
     * Constructor, getter & setter.
     */
    public RDrawableNode(String name) {
        this.name = name;
        this.totalWeight = 0.0d;
        this.valueNodeList = new ArrayList<>();
    }

    public void setValueNodeList(List<ValueDrawableNode> valueNodeList) {
        this.valueNodeList = valueNodeList;
    }

    public String getValue() {
        return name;
    }

    public Double getTotalWeight() {
        return totalWeight / valueNodeList.size();
    }

    public ClassDrawableNode getClassNode() {
        return classNode;
    }

    @Override
    public int compareTo(RDrawableNode o) {
        return totalWeight.compareTo(o.getTotalWeight());
    }

    @Override
    public void onResetValue() {
        totalWeight = 0.0d;
    }

    /**
     * Adding new class node.
     *
     * @param classNode
     */
    public void addClassNode(ClassDrawableNode classNode) {
        if (this.classNode == null)
            this.classNode = classNode;
    }

    /**
     * Adding next value node to total wage value.
     *
     * @param wage
     */
    public void addToTotalWage(Double wage) {
        totalWeight = totalWeight + wage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RDrawableNode rNode = (RDrawableNode) o;

        return name.equals(rNode.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
