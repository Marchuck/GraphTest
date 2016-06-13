package topics.agds.nodes;

import agds_core.AGDSConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class GenericClassNode<T extends Comparable<T>> extends AbstractNode {

    private List<GenericRecordNode<T>> rNodeList = new ArrayList<>();

    public GenericClassNode(String name) {
        super(name);
    }

    public GenericClassNode addNode(GenericRecordNode<T> rNode) {
        this.rNodeList.add(rNode);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDSConstants.CLASS_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDSConstants.CLASS_NODE_WEIGHT;
    }

    // @Override
    public List<GenericRecordNode<T>> getNodes() {
        return rNodeList;
    }

    public void sort() {
        //sort reverse
        Collections.sort(rNodeList, new Comparator<GenericRecordNode<T>>() {
            @Override
            public int compare(GenericRecordNode<T> o1, GenericRecordNode<T> o2) {
                return o1.compareTo(o2);
            }
        });

    }

    public void clean() {
        for (GenericRecordNode<T> rNode : rNodeList) rNode.clean();
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }
}
