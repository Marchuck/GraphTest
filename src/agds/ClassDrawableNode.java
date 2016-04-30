package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassDrawableNode implements DrawableNode {

    private String className;
    private List<RDrawableNode> rNodeList;


    /**
     * Constructor, getter & setter.
     */
    public ClassDrawableNode(String className) {
        this.className = className;
        this.rNodeList = new ArrayList<>();
    }

    public void addRecordNode(RDrawableNode rNode) {
        if (!rNodeList.contains(rNode))
            rNodeList.add(rNode);
    }

    public List<RDrawableNode> getrNodeList() {
        return rNodeList;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Sorting nodes in descending order - first element is the most similar element from that class.
     */
    public void sortNodes() {
        Collections.sort(rNodeList, Collections.<RDrawableNode>reverseOrder());
    }

    /**
     * Reseting record nodes wage values.
     */
    public void resetRecordNodes() {
        for (RDrawableNode rNode : rNodeList)
            rNode.onResetValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassDrawableNode that = (ClassDrawableNode) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.CLASS_NODE_STYLESHEET;
    }
    @Override
    public int getEdgeWeight() {
        return AGDS.CLASS_NODE_WEIGHT;
    }

}
