package some_graphs;

import agds.AGDS;
import agds.DrawableNode;
import agds.GraphDrawer;
import common.Log;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import java.util.Iterator;

/**
 * @author lukasz
 * @since 01.04.16.
 */
public class GraphVisualiser implements GraphDrawer<DrawableNode> {
    public static final String TAG = GraphVisualiser.class.getSimpleName();
    public boolean isLegend = false;

    public Graph getGraph() {
        return graph;
    }

    public static final int summaryBuildingTimeInMillis = 10000;

    private static final String GRAPH_STYLESHEET = "node {buildGraph-color: rgb(0,0,255);}" +
            "node.marked {buildGraph-color: rgb(255,0,0);}";

    private boolean notDrawed = true;

    private String previousNodeTag;
    private Graph graph;
    private Viewer currentViewer;

    public GraphVisualiser(String name) {
//        graph = new SingleGraph(name);
        graph = new MultiGraph(name);
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
        e.addAttribute("ui.label", id);
    }


    /**
     * "Walking" over whole graph, from one neighbour to another, starting from source AGDSNode
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

    public boolean containsEdge(DrawableNode drawableNodeA, DrawableNode drawableNodeB) {
        return graph.getEdge(drawableNodeA.getName() + "" + drawableNodeB.getName()) != null;
    }

    public boolean containsNode(DrawableNode drawableNodeA) {
        return graph.getNode(drawableNodeA.getName()) != null;
    }

    public Viewer showGraph() {
        currentViewer = graph.display();
        if (isLegend) showLegend();
        return currentViewer;
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

    /**
     * try to use interface instead
     *
     * @param value
     * @param styleSheet
     * @return
     */
    @Deprecated
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

    private static final String RECORD_NODES = "Record Nodes";
    private static final String ATTR_NODES = "Attribute Nodes";
    private static final String CLASS_NODES = "Class Nodes";
    private static final String VALUE_NODES = "Value Nodes";

    private synchronized void showLegend() {
        Node a = drawNode(RECORD_NODES, AGDS.RECORD_NODE_STYLESHEET);
        Node b = drawNode(ATTR_NODES, AGDS.PROPERTY_NODE_STYLESHEET);
        Node c = drawNode(CLASS_NODES, AGDS.CLASS_NODE_STYLESHEET);
        Node d = drawNode(VALUE_NODES, AGDS.VALUE_NODE_STYLESHEET);
        graph.addEdge(a.getId() + b.getId(), a.getId(), b.getId());
        graph.addEdge(c.getId() + b.getId(), c.getId(), b.getId());
        graph.addEdge(c.getId() + d.getId(), c.getId(), d.getId());
    }

    private synchronized void removeLegend() {
        if (graph.getNode(RECORD_NODES) != null)
            graph.removeNode(RECORD_NODES);
        if (graph.getNode(ATTR_NODES) != null)
            graph.removeNode(ATTR_NODES);
        if (graph.getNode(VALUE_NODES) != null)
            graph.removeNode(VALUE_NODES);
        if (graph.getNode(CLASS_NODES) != null)
            graph.removeNode(CLASS_NODES);
    }

    /**
     * can be triggered from button
     */
    public synchronized void switchLegend() {
        if (isLegend) {
            removeLegend();
            disableLegend();
        } else {
            showLegend();
            enableLegend();
        }
    }

    /**
     * Add new node to existing graph
     * Don't worry about adding the same node multiple times, because it's impossible
     *
     * @param drawableNode
     */
    @Override
    public void drawNode(DrawableNode drawableNode) {
        String nodeTag = drawableNode.getName();

        if (!this.containsNode(drawableNode)) {
            //adding new drawableNode
            Node newNode = graph.addNode(nodeTag);
            //appearance customizations
            String id = newNode.getId();
            newNode.addAttribute("ui.label", id);
            newNode.addAttribute("ui.style", drawableNode.getStyleSheet());
        } else {
            Log.e("Cannot draw drawableNode! DrawableNode " + nodeTag + " already exists.");
        }
    }

    /**
     * Add new edge
     *
     * @param drawableNodeA
     * @param drawableNodeB
     */
    @Override
    public void drawEdge(DrawableNode drawableNodeA, DrawableNode drawableNodeB) {
        String a = drawableNodeA.getName();
        String b = drawableNodeB.getName();
        String ab = a.concat(b);

        if (!this.containsEdge(drawableNodeA, drawableNodeB)) {
            //adding new edge
            Edge newEdge = graph.addEdge(ab, a, b);
            //appearance customizations

            newEdge.setAttribute("layout.weight", drawableNodeA.getEdgeWeight() * drawableNodeA.getEdgeWeight());
        } else {
            Log.e("Cannot draw edge! Edge " + ab + " already exists.");
        }

    }

    public void enableLegend() {
        isLegend = true;
    }

    public synchronized void disableLegend() {
        isLegend = false;
    }

    public void hideGraph() {
        System.out.println("hideGraph");
        currentViewer.close();
        currentViewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
    }
}
