package topics.agds.nodes;

import agds.AGDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 25.04.16.
 */
public class ClassNode extends AbstractNode {

    public ClassNode(String name) {
        super(name);
    }

    private List<AbstractNode> rNodeList = new ArrayList<>();

    @Override
    public ClassNode addNode(AbstractNode rNode) {
        this.rNodeList.add(rNode);
        return this;
    }

    public ClassNode addNodes(AbstractNode... rNodes) {
        Collections.addAll(rNodeList, rNodes);
        return this;
    }

    public List<AbstractNode> getrNodeList() {
        return rNodeList;
    }

    @Override
    public AbstractNode sort() {
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

    @Override
    public List<AbstractNode> getNodes() {
        return rNodeList;
    }

    @Override
    public int compareTo(AbstractNode o) {
        return 0;
    }
}
