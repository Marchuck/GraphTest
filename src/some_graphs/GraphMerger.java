package some_graphs;

import agds.AGDS;
import agds.GraphDrawer;
import agds.Node;
import common.Log;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;

/**
 * @author Lukasz Marczak
 * @since 12.04.16.
 * Put together drawing graph and control parameters with buttons and other ui widgets
 */
public class GraphMerger extends JComponent {
    public static final String TAG = GraphMerger.class.getSimpleName();

    private AGDS agds;
    private GraphVisualiser graphVisualiser;

    public AGDS getAgds() {
        return agds;
    }

    public void setAgds(AGDS agds) {
        this.agds = agds;
    }

    public GraphVisualiser getGraphVisualiser() {
        return graphVisualiser;
    }

    public void setGraphVisualiser(GraphVisualiser graphVisualiser) {
        this.graphVisualiser = graphVisualiser;
    }

    public GraphMerger() {
        graphVisualiser = new GraphVisualiser("AGDS visualiser");
//        graphVisualiser.setRefreshCount(10).setSleepTime(3000);
        graphVisualiser.setStepsDisabled(false);
        agds = new AGDS();
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
//        graphVisualiser.showGraphWithLegend();
        //graphVisualiser.showGraph();

        Viewer viewer = new Viewer(graphVisualiser.getGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

//        viewer.addDefaultView(true);
        viewer.enableAutoLayout();
//        add(viewer.addDefaultView(false), BorderLayout.CENTER);

        showGraph();
    }

    public void showGraph() {
        graphVisualiser.showGraph();
    }

}
