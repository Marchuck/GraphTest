import agds.AGDS;
import agds.GraphDrawer;
import agds.Node;
import common.DataReader;
import common.Log;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import some_graphs.AGDS_Visualiser;
import some_graphs.GraphVisualiser;

import javax.swing.*;
import java.awt.*;

/**
 * @author lukasz
 * @since 23.03.16.
 */
public class Main {
    public static final String TAG = Main.class.getSimpleName();

    static boolean runExample = false;

    public static void main(String[] args) {
        if (runExample) {
            new AGDS_Visualiser(10).run();
            return;
        }

        final GraphVisualiser graphVisualiser = new GraphVisualiser("AGDS visualiser");
//        graphVisualiser.setStepsDisabled(false);
        AGDS agds = new AGDS().withData(DataReader.IRIS_DATA);
        agds.connectGraphDrawer(new GraphDrawer<Node>() {

            @Override
            public void drawNode(Node node) {
                Log.d(TAG, "drawNode " + node.getValue() + "," + node.getStyleSheet());
                if (!graphVisualiser.containsNode(node))
                    graphVisualiser.drawNode(node.getValue(), node.getStyleSheet());
                else
                    Log.e(TAG, "drawNode failed. AGDSNode " + node.getValue() + " already exists!");
            }

            @Override
            public void drawEdge(Node nodeA, Node nodeB) {
                Log.d(TAG, "drawEdge " + nodeA.getValue() + "," + nodeB.getValue());
                if (!graphVisualiser.containsEdge(nodeA, nodeB))
                    graphVisualiser.drawEdge(nodeA.getValue(), nodeB.getValue());
                else
                    Log.e(TAG, "drawNode failed. Edge (" + nodeA.getValue()
                            + "," + nodeB.getValue() + ") already exists!");
            }
        });
        agds.drawData();
        graphVisualiser.showGraph();
    }

    public void draw() {
        // On est dans le thread main.

        Graph graph = new MultiGraph("mg");

        // On demande au viewer de consid�rer que le graphe ne sera lu et modifi� que
        // dans le thread Swing.

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

        // � Partir de l�, le viewer consid�re que le graphe est dans son propre thread,
        // c'est-�-dire le thread Swing. Il est donc dangereux d'y toucher dans la thread
        // main. On utilise invokeLater pour faire tourner du code dans le thread Swing,
        // par exemple pour initialiser l'application :

        SwingUtilities.invokeLater(new InitializeApplication(viewer, graph));
    }

    static class InitializeApplication extends JFrame implements Runnable {
        private static final long serialVersionUID = -804177406404724792L;
        protected Graph graph;
        protected Viewer viewer;

        public InitializeApplication(Viewer viewer, Graph graph) {
            this.viewer = viewer;
            this.graph = graph;
        }

        public void run() {

            /*
            graph.addNode("A");
            graph.addNode("B");
            graph.addNode("C");
            graph.addNode("D");
            graph.addNode("E");
            graph.addNode("F");
            graph.addEdge("AB", "A", "B");
            graph.addEdge("BC", "B", "C");
            graph.addEdge("CA", "C", "A");
            graph.addEdge("FA", "F", "A");
            graph.addAttribute("ui.antialias");
            graph.addAttribute("ui.quality");
            graph.addAttribute("ui.stylesheet", styleSheet);

            graph.getNode("A").setAttribute("xyz", -1, 0, 0);
            graph.getNode("B").setAttribute("xyz", 1, 0, 0);
            graph.getNode("C").setAttribute("xyz", 0, 1, 0);
            graph.getNode("D").setAttribute("xyz", 4, 1, 0);
            graph.getNode("E").setAttribute("xyz", 3, 1, 0);
            graph.getNode("F").setAttribute("xyz", 2, 1, 0);

            // On ins�re la vue principale du viewer dans la JFrame.
*/
            add(viewer.addDefaultView(false), BorderLayout.CENTER);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setSize(800, 600);
            setVisible(true);
        }

        protected String styleSheet =
                "graph {padding: 60px; }";
    }
}
