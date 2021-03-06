package topics.agds.nodes;

import agds_core.DrawableNode;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public abstract class AbstractNode implements Comparable<AbstractNode>, DrawableNode {

    public static final String NOT_CLASSIFIED = "Not classified";

    protected String name;

    public AbstractNode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
//        Utils.log("name: " + name);
        return name;
    }


}
