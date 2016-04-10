package agds;

/**
 * Created by lukasz on 04.04.16.
 */
public interface GraphDrawer<T> {
    void drawNode(T nodeName);

    void drawEdge(T nodeA, T nodeB);
}