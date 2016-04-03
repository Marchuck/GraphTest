package some_graphs;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import util.Log;

import java.util.Iterator;

/**
 * @author lukasz
 * @since 01.04.16.
 */
public class AGDS_Visualiser {
    public static final String TAG = AGDS_Visualiser.class.getSimpleName();

    public static final int summaryBuildingTimeInMillis = 10000;

    private static final String GRAPH_STYLESHEET = "node {fill-color: rgb(0,0,255);}" +
            "node.marked {fill-color: rgb(255,0,0);}";

    private boolean notDrawed = true;

    private int nodes;

    public AGDS_Visualiser(int nodes) {
        this.nodes = nodes;
    }

    public AGDS_Visualiser() {
        this(6);
    }

    private void doOnNext(Graph graph, int nextInt, int n) {
        Log.d(TAG, "doOnNext " + nextInt + "," + n);
        if (nextInt == 0) {
            //this looks tricky, but it is equvalent to
            //Element element = graph.addNode("X");
            //addLabel(element);
            addLabel(graph.addNode("R"));
            addLabel(graph.addNode("A"));
            addLabel(graph.addEdge("RA", "A", "R"));
            addLabel(graph.addNode("B"));
        } else if (nextInt < n) {
            String newNodeName = "A" + nextInt;
            Node newNodeAn = graph.addNode(newNodeName);
            addLabel(newNodeAn);
            Edge edgeAnA = graph.addEdge("A" + newNodeName, "A", newNodeName);
            addLabel(edgeAnA);
            Edge edgeAnB = graph.addEdge("B" + newNodeName, "B", newNodeName);
            addLabel(edgeAnB);
        } else if (n == nextInt) {
            addLabel(graph.addNode("C"));
        } else {
            if (notDrawed) {
                notDrawed = false;
                addLabel(graph.addEdge("CB", "C", "B"));
            }
            Log.d(TAG, "completed");
        }
    }

    /**
     * Adds label for each graph which is drawn
     *
     * @param e
     */
    public static void addLabel(Element e) {
        String id = e.getId();
        e.addAttribute("ui.label", id);
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

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
        }
    }

    public void run() {
        final Graph graph = new SingleGraph("Basic drawing");
        graph.setStrict(true);
        graph.setAutoCreate(true);

        graph.addAttribute("ui.stylesheet", GRAPH_STYLESHEET);
        int maxStep = nodes;
//        graph.display();
        for (int j = 0; j < maxStep; j++) {
            doOnNext(graph, j, maxStep);
//            sleep(summaryBuildingTimeInMillis / maxStep);
        }

        graph.display();
        sleep(2000);
        explore(graph.getNode("A"));
    }
}
