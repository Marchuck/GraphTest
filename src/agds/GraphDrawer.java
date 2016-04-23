package agds;

/**
 * @author Lukasz Marczak
 * @since 04.04.16.
 */
public interface GraphDrawer<T> {
    void drawNode(T nodeName);

    void drawEdge(T nodeA, T nodeB);
}