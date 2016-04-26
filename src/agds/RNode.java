package agds;

import java.util.ArrayList;
import java.util.List;

public class RNode implements Node, Comparable<RNode>, Resetable {

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.RECORD_NODE_STYLESHEET;
    }

    private String name;
    private Double totalWeight;
    private ClassNode classNode;
    private List<ValueNode> valueNodeList;

    /**
     * Constructor, getter & setter.
     */
    public RNode(String name) {
        this.name = name;
        this.totalWeight = 0.0d;
        this.valueNodeList = new ArrayList<>();
    }

    public void setValueNodeList(List<ValueNode> valueNodeList) {
        this.valueNodeList = valueNodeList;
    }

    public String getName() {
        return name;
    }

    public Double getTotalWeight() {
        return totalWeight / valueNodeList.size();
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    @Override
    public int compareTo(RNode o) {
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
    public void addClassNode(ClassNode classNode) {
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

        RNode rNode = (RNode) o;

        return name.equals(rNode.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
