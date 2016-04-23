package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Double totalWage;
    private ClassNode classNode;
    private List<ValueNode> valueNodeList;

    /**
     * Constructor, getter & setter.
     */
    public RNode(String name) {
        this.name = name;
        this.totalWage = 0.0d;
        this.valueNodeList = new ArrayList<>();
    }

    public void setValueNodeList(List<ValueNode> valueNodeList) {
        this.valueNodeList = valueNodeList;
    }

    public String getName() {
        return name;
    }

    public Double getTotalWage() {
        return totalWage / valueNodeList.size();
    }

    public ClassNode getClassNode() {
        return classNode;
    }

    @Override
    public int compareTo(RNode o) {
        return totalWage.compareTo(o.getTotalWage());
    }

    @Override
    public void onResetValue() {
        totalWage = 0.0d;
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
        totalWage = totalWage + wage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RNode that = (RNode) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}
