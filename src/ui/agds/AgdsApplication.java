package ui.agds;

import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import topics.agds.AGDSAlgorithm;
import topics.agds.GenericAgdsEngine;
import topics.agds.SourceSet;
import ui.connector.GraphCallbacks;

import javax.swing.*;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class AgdsApplication implements Runnable {

    private static AgdsApplication instance;
    private JFrame currentGraphInstance;
    public AGDSAlgorithm algorithm;
    public AgdsGUI gui;

    public static AgdsApplication getInstance() {
        return instance == null ? new AgdsApplication() : instance;
    }

    @Override
    public void run() {
        gui = new AgdsGUI("Methods of Knowledge Engineering");
        algorithm = new AGDSAlgorithm(SourceSet.Iris);
        setupCallbacks();
        algorithm.run();
    }

    private void setupCallbacks() {
        algorithm.graphHandler = new GraphCallbacks() {
            @Override
            public void onEngineCreated(GenericAgdsEngine engine) {
                gui.setAgdsEngine(engine);
            }

            @Override
            public void onGraphCreated(Viewer viewer) {
                gui.graphViewer = viewer;
            }

            @Override
            public void onVisualiserCreated(GraphVisualiser graphVisualiser) {
                gui.agdsVisualiser = graphVisualiser;
            }
        };
    }

    public static void main(String[] args) {
        getInstance().run();
    }
}
