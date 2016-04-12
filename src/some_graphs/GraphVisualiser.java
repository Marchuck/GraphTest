package some_graphs;

import agds.AGDS;
import agds.GraphDrawer;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

/**
 * @author lukasz
 * @since 01.04.16.
 */
public class GraphVisualiser {
    public static final String TAG = GraphVisualiser.class.getSimpleName();
    public boolean isLegend = false;

    public Graph getGraph() {
        return graph;
    }

    public static final int summaryBuildingTimeInMillis = 10000;

    private static final String GRAPH_STYLESHEET = "node {fill-color: rgb(0,0,255);}" +
            "node.marked {fill-color: rgb(255,0,0);}";

    private boolean notDrawed = true;

    private String previousNodeTag;
    private Graph graph;

    public GraphVisualiser(String name) {
//        graph = new SingleGraph(name);
        graph = new MultiGraph(name);
    }


    public void drawNode(String nodeTag) {
        addElementWithLabel(graph.addNode(nodeTag));
    }

    public void drawEdge(String firstNodeTag, String secondNodeTag) {
        addElementWithLabel(graph.addEdge(firstNodeTag + secondNodeTag, firstNodeTag, secondNodeTag));
    }

    public void displayGraph() {
        graph.display();
    }

    /**
     * Adds label for each graph which is drawn
     *
     * @param e
     */
    public static void addElementWithLabel(Element e) {
        String id = e.getId();
//        e.addAttribute("ui.label", id);
    }

    /**
     * "Walking" over whole graph, from one neighbour to another, starting from source Node
     *
     * @param source initial node from which exploration starts
     */
    public static void explore(Node source) {
        Iterator<? extends Node> k = source.getBreadthFirstIterator();
        Node currentNode = null;
        while (k.hasNext()) {
            if (currentNode != null) {
                currentNode.setAttribute("ui.class", "unmarked");
            }
            Node next = k.next();
            currentNode = next;
            next.setAttribute("ui.class", "marked");
            sleep(700);
        }
    }

    public void unmarkAllNodes() {
        for (Node node : graph.getNodeSet())
            node.setAttribute("ui.class", "unmarked");
    }

    public Node unmarkNode(String nodeTag) {
        Node unselectedNode = graph.getNode(nodeTag);
        unselectedNode.setAttribute("ui.class", "unmarked");
        return unselectedNode;
    }

    public Node markNode(String nodeTag) {
        Node selectedNode = graph.getNode(nodeTag);
        selectedNode.setAttribute("ui.class", "marked");
        return selectedNode;
    }

    public void markNodeWithUnselection(String nodeTag) {
        if (previousNodeTag != null)
            unmarkNode(previousNodeTag);
        markNode(nodeTag);
    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ignored) {
        }
    }

    public boolean containsEdge(agds.Node nodeA, agds.Node nodeB) {
        return graph.getEdge(nodeA.getValue() + "" + nodeB.getValue()) != null;
    }

    public boolean containsNode(agds.Node nodeA) {
        return graph.getNode(nodeA.getValue()) != null;
    }

    public void showGraph() {
        graph.display();
        if (isLegend) showLegend();
    }


    int invalidatorIndex = 0;
    int REFRESH_COUNT = 10;
    int SLEEP_TIME = 2000;
    boolean stepsEnabled = true;

    public GraphVisualiser setRefreshCount(int c) {
        REFRESH_COUNT = c;
        return this;
    }

    public GraphVisualiser setSleepTime(int t) {
        SLEEP_TIME = t;
        return this;
    }

    public GraphVisualiser setStepsDisabled(boolean enabled) {
        this.stepsEnabled = enabled;
        return this;
    }

    public Node drawNode(String value, String styleSheet) {
        Node node = graph.addNode(value);
        String id = node.getId();
        node.addAttribute("ui.label", id);
        node.addAttribute("ui.style", styleSheet);
        if (stepsEnabled) {
            if (invalidatorIndex == REFRESH_COUNT) {
                sleep(SLEEP_TIME);
                graph.display();
                invalidatorIndex = 0;
            }
            ++invalidatorIndex;
        }
        return node;
    }

    public static final String RECORD_NODES = "Record Nodes";
    public static final String ATTR_NODES = "Attribute Nodes";
    public static final String CLASS_NODES = "Class Nodes";
    public static final String VALUE_NODES = "Value Nodes";

    public void showLegend() {
        Node a = drawNode(RECORD_NODES, AGDS.RECORD_NODE_STYLESHEET);
        Node b = drawNode(ATTR_NODES, AGDS.ATTR_NODE_STYLESHEET);
        Node c = drawNode(CLASS_NODES, AGDS.CLASS_NODE_STYLESHEET);
        Node d = drawNode(VALUE_NODES, AGDS.VALUE_NODE_STYLESHEET);
        graph.addEdge(a.getId() + b.getId(), a.getId(), b.getId());
        graph.addEdge(c.getId() + b.getId(), c.getId(), b.getId());
        graph.addEdge(c.getId() + d.getId(), c.getId(), d.getId());
    }

    public void removeLegend() {
        if (graph.getNode(RECORD_NODES) != null)
            graph.removeNode(RECORD_NODES);
        if (graph.getNode(ATTR_NODES) != null)
            graph.removeNode(ATTR_NODES);
        if (graph.getNode(VALUE_NODES) != null)
            graph.removeNode(VALUE_NODES);
        if (graph.getNode(CLASS_NODES) != null)
            graph.removeNode(CLASS_NODES);
    }

    public synchronized void switchLegend() {
        if (isLegend) {
            removeLegend();
            isLegend = false;
        } else {
            showLegend();
            isLegend = true;
        }
    }
}
