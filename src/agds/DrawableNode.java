package agds;

public interface DrawableNode {
    /**
     * @return name visible at the graph peak
     */
    String getName();
    /**
     * @return stylesheet for sepcified node. For example {@link AGDS#CLASS_NODE_STYLESHEET}
     */
    String getStyleSheet();

    /**
     * @return informs graph drawing utility how long should be edge
     * For example: {@link AGDS#CLASS_NODE_WEIGHT}
     */
    int getEdgeWeight();
}
