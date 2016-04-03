import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import some_graphs.AGDS_Visualiser;
import ui.NDimensionalPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author lukasz
 * @since 23.03.16.
 */
public class Main {
    public static final String TAG = Main.class.getSimpleName();

    public static JComponent createEmptyComponent(String name) {
        return NDimensionalPanel.makeTextPanel(name);
    }


    public static void main(String[] args) {

        new AGDS_Visualiser().run();

//   new DemoViewerJComponents();
        // NDimensionalPanel.showTabs(new SingleTab("KNN", createEmptyComponent("//TODO: KNN")),
        //       new SingleTab("Data Mining", createEmptyComponent("TODO: DATA MINING")));
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
