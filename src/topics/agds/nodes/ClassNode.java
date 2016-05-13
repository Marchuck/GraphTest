package topics.agds.nodes;

import agds.AGDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class ClassNode extends AbstractNode {

    public ClassNode(String name) {
        super(name);
    }

    private List<RecordNode> rNodeList = new ArrayList<>();

    public ClassNode addNode(RecordNode rNode) {
        this.rNodeList.add(rNode);
        return this;
    }

    @Override
    public String getStyleSheet() {
        return AGDS.CLASS_NODE_STYLESHEET;
    }

    @Override
    public int getEdgeWeight() {
        return AGDS.CLASS_NODE_WEIGHT;
    }

    // @Override
    public List<RecordNode> getNodes() {
        return rNodeList;
    }

    public void sort() {
        //sort reverse
        Collections.sort(rNodeList, new Comparator<RecordNode>() {
            @Override
            public int compare(RecordNode o1, RecordNode o2) {
                return -Double.compare(o1.getTotalWeight(), o2.getTotalWeight());
            }
        });

    }
    public void clean() {
        for (RecordNode rNode : rNodeList) rNode.clean();
    }
    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }
}
