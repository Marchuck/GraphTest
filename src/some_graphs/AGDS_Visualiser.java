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

    private static final String GRAPH_STYLESHEET = "node#A {fill-color: rgb(0,0,255);}" +
            "node#B {fill-color: rgb(255,0,0);}" +
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
            //addElementWithLabel(element);
            addLabel(graph.addNode("R"), nextInt);
            addLabel(graph.addNode("A"), nextInt);
            addLabel(graph.addEdge("RA", "A", "R"), nextInt);
            addLabel(graph.addNode("B"), nextInt);


        } else if (nextInt < n) {
            String newNodeName = "A" + nextInt;
            Node newNodeAn = graph.addNode(newNodeName);
            addLabel(newNodeAn, nextInt);
            Edge edgeAnA = graph.addEdge("A" + newNodeName, "A", newNodeName);
            addLabel(edgeAnA, nextInt);
            Edge edgeAnB = graph.addEdge("B" + newNodeName, "B", newNodeName);
            addLabel(edgeAnB, nextInt);
        } else if (n == nextInt) {
            addLabel(graph.addNode("C"), nextInt);
        } else {
            if (notDrawed) {
                notDrawed = false;
                addLabel(graph.addEdge("CB", "C", "B"), nextInt);
            }
            Log.d(TAG, "completed");
        }
    }

    /**
     * Adds label for each graph which is drawn
     *
     * @param e
     */
    public static void addLabel(Element e, int index) {
        String id = e.getId();
        e.addAttribute("ui.label", id);

        if (index % 2 == 0) e.addAttribute("ui.style", "fill-color: rgb(255,0,0);");
        else e.addAttribute("ui.style", "fill-color: rgb(0,255,0);");


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

//        graph.addAttribute("ui.stylesheet", GRAPH_STYLESHEET);
        int maxStep = nodes;
//        graph.display();
        for (int j = 0; j < maxStep; j++) {
            doOnNext(graph, j, maxStep);
            graph.display();
            sleep(500);

        }

//        graph.display();
//        sleep(2000);
//        explore(graph.getNode("A"));
    }
}
