package ui;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import util.Log;

import javax.swing.*;
import java.awt.*;

/**
 * @author Lukasz Marczak
 * @since 12.04.16.
 */
public class GraphView extends JComponent {

    public static final String TAG = GraphView.class.getSimpleName();

    public GraphView() {
        Log.d(TAG, "GraphView ");
        GraphVisualiser graphVisualiser = new GraphVisualiser("Graph visualizer");

        Viewer viewer = new Viewer(graphVisualiser.getGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);

//        viewer.addDefaultView(true);
        viewer.enableAutoLayout();
        add(viewer.addDefaultView(true), BorderLayout.CENTER);
    }
}
