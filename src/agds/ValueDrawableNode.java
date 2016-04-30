package agds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ValueDrawableNode implements DrawableNode, Comparable<ValueDrawableNode>, Resetable {

    private Double value;
    private Double wage;
    private List<RDrawableNode> rNodeList;
    private PropertyDrawableNode propertyNode;

    /**
     * Constructor, getter & setter.
     */

    public ValueDrawableNode(Double value) {
        this.value = value;
        this.rNodeList = new ArrayList<>();
    }


    public void setPropertyNode(PropertyDrawableNode propertyNode) {
        this.propertyNode = propertyNode;
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }

    public Double getDoubleValue() {
        return value;
    }

    public String getName() {
        return String.valueOf(value);
    }

    @Override
    public String getStyleSheet() {
        return AGDS.VALUE_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDS.VALUE_NODE_WEIGHT;
    }

    public List<RDrawableNode> getrNodeList() {
        return rNodeList;
    }

    /**
     * Adding calculated wage to all associated record nodes.
     *
     * @param wage
     */
    public void addCalcuatedWeightToAllRecords(Double wage) {
        setWage(wage);
        for (RDrawableNode rNode : rNodeList) {
            rNode.addToTotalWage(wage);
        }
    }

    /**
     * Adding new record node.
     *
     * @param rNode
     */
    public void addRecordNode(RDrawableNode rNode) {
        rNodeList.add(rNode);
    }

    /**
     * Interface methods
     */

    @Override
    public int compareTo(ValueDrawableNode o) {
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
        ValueDrawableNode that = (ValueDrawableNode) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
