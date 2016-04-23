package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ValueNode implements Node, Comparable<ValueNode>, Resetable {

    private Double value;
    private Double wage;
    private List<RNode> rNodeList;
    private AttributeNode attributeNode;

    /**
     * Constructor, getter & setter.
     */

    public ValueNode(Double value) {
        this.value = value;
        this.rNodeList = new ArrayList<>();
    }


    public void setAttributeNode(AttributeNode attributeNode) {
        this.attributeNode = attributeNode;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Double getDoubleValue() {
        return value;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    @Override
    public String getStyleSheet() {
        return AGDS.VALUE_NODE_STYLESHEET;
    }

    public List<RNode> getrNodeList() {
        return rNodeList;
    }

    /**
     * Adding calculated wage to all associated record nodes.
     *
     * @param wage
     */
    public void addCalcuatedWeightToAllRecords(Double wage) {
        setWage(wage);
        for (RNode rNode : rNodeList) {
            rNode.addToTotalWage(wage);
        }
    }

    /**
     * Adding new record node.
     *
     * @param rNode
     */
    public void addRecordNode(RNode rNode) {
        rNodeList.add(rNode);
    }

    /**
     * Interface methods
     */

    @Override
    public int compareTo(ValueNode o) {
        return value.compareTo(o.getDoubleValue());
    }

    @Override
    public void onResetValue() {
        wage = 0.0d;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueNode that = (ValueNode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
