package some_graphs;

import common.Log;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Iterator;

/**
 * @author lukasz
 * @since 01.04.16.
 */
public class AGDS_Visualiser {
    public static final String TAG = AGDS_Visualiser.class.getSimpleName();

    public static void main(String[] args) {
        new AGDS_Visualiser(10).run();
    }

    public static final int summaryBuildingTimeInMillis = 10000;

    private static final String GRAPH_STYLESHEET = "node#A {buildGraph-color: rgb(0,0,255);}" +
            "node#B {buildGraph-color: rgb(255,0,0);}" +
            "node.marked {buildGraph-color: rgb(255,0,0);}";


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
//        e.setAttribute("ui.style", "buildGraph-color: rgb(255,0,0);");
//        if (index % 2 == 0) e.setAttribute("ui.style", "buildGraph-color: rgb(255,0,0);");
//        else e.setAttribute("ui.style", "buildGraph-color: rgb(0,0,255);");


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
            sleep(50);

        }

//        graph.display();
//        sleep(2000);
        GraphVisualiser.explore(graph.getNode("A"));
    }
}
