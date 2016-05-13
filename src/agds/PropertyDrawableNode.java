package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PropertyDrawableNode implements DrawableNode {

    private final String value;
    private List<ValueDrawableNode> valueNodeList;
    private ValueDrawableNode minValueNode;
    private ValueDrawableNode maxValueNode;

    /**
     * Constuctrors, getters & setters
     */

    public PropertyDrawableNode(String value) {
        this.value = value;
        this.valueNodeList = new ArrayList<>();
    }
@Override
    public String getName() {
        return value;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.PROPERTY_NODE_STYLESHEET;
    }
    @Override
    public int getEdgeWeight() {
        return AGDS.PROPERTY_NODE_WEIGHT;
    }


    public List<ValueDrawableNode> getValueNodeList() {
        return valueNodeList;
    }

    public ValueDrawableNode getMinValueNode() {
        return minValueNode;
    }

    public ValueDrawableNode getMaxValueNode() {
        return maxValueNode;
    }

    /**
     * Adding new value node to attribute list.
     *
     * @param valueNode - value candidate
     */
    public void addNode(ValueDrawableNode valueNode) {

        if (valueNodeList.contains(valueNode)) {
            ValueDrawableNode foundValueNode = valueNodeList.get(valueNodeList.indexOf(valueNode));
            foundValueNode.getrNodeList().addAll(valueNode.getrNodeList());
        } else
            valueNodeList.add(valueNode);
    }


    /**
     * Sorting value nodes in ascending order.
     */
    public void sortValueNodes() {
        Collections.sort(valueNodeList);
        assignMinValueNode();
        assignMaxValueNode();
    }

    /**
     * Calculating wages based on most similar element in index
     */
    public void calculateWages(int indexValue) {
        for (ValueDrawableNode valueNode : getValueNodeList()) {
            double wageValue = 1 - (Math.abs(valueNode.getDoubleValue()
                    - getValueNodeList().get(indexValue).getDoubleValue()))
                    / (maxValueNode.getDoubleValue() - minValueNode.getDoubleValue());
            valueNode.addCalcuatedWeightToAllRecords(wageValue);
        }
    }

    public static void calculateWages(PropertyDrawableNode node,int indexValue) {
        for (ValueDrawableNode valueNode : node.getValueNodeList()) {
            double maxValueNode = node.getMaxValueNode().getDoubleValue();
            double minValueNode = node.getMinValueNode().getDoubleValue();

            double wageValue = 1 - (Math.abs(valueNode.getDoubleValue() - node.getValueNodeList()
                    .get(indexValue).getDoubleValue())) / (maxValueNode - minValueNode);
            valueNode.addCalcuatedWeightToAllRecords(wageValue);
        }
    }

    /**
     * Reseting value nodes wage value.
     */
    public void resetValueNodes() {
        for (ValueDrawableNode valueNode : valueNodeList)
            valueNode.onResetValue();
    }

    /**
     * Keeping reference to minimal value node.
     */
    private void assignMinValueNode() {
        minValueNode = valueNodeList.get(0);
    }

    /**
     * Keeping reference to maximal value node.
     */
    private void assignMaxValueNode() {
        maxValueNode = valueNodeList.get(valueNodeList.size() - 1);
    }
}
