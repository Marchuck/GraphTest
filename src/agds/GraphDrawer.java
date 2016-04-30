package agds;

/**
 * @author Lukasz Marczak
 * @since 04.04.16.
 */
public interface GraphDrawer<T extends DrawableNode> {
    void drawNode(T nodeName);

    void drawEdge(T nodeA, T nodeB);

}