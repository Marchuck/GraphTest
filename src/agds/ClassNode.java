package agds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassNode implements Node {

    private String className;
    private List<RecordNode> recordNodeList;


    /**
     * Constructor, getter & setter.
     */
    public ClassNode(String className) {
        this.className = className;
        this.recordNodeList = new ArrayList<>();
    }

    public void addRecordNode(RecordNode recordNode) {
        if (!recordNodeList.contains(recordNode))
            recordNodeList.add(recordNode);
    }

    public List<RecordNode> getRecordNodeList() {
        return recordNodeList;
    }

    public String getClassName() {
        return className;
    }

    /**
     * Sorting nodes in descending order - first element is the most similar element from that class.
     */
    public void sortNodes() {
        Collections.sort(recordNodeList, Collections.<RecordNode>reverseOrder());
    }

    /**
     * Reseting record nodes wage values.
     */
    public void resetRecordNodes() {
        for (RecordNode recordNode : recordNodeList)
            recordNode.onResetValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassNode that = (ClassNode) o;
        return Objects.equals(className, that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className);
    }

    @Override
    public String getValue() {
        return className;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.CLASS_NODE_STYLESHEET;
    }
}
