package ui.agds;

import common.Utils;
import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import topics.agds.AGDSAlgorithm;
import topics.agds.SourceSet;
import topics.agds.engine.AgdsEngine;
import ui.connector.GraphCallbacks;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class AgdsApplication implements Runnable {

    private static AgdsApplication instance;
    public AGDSAlgorithm algorithm;
    public AgdsGUI gui;

    public static AgdsApplication getInstance() {
        if (instance == null) instance = new AgdsApplication();
        return instance;
    }

    @Override
    public void run() {
        gui = new AgdsGUI("Methods of Knowledge Engineering");
        algorithm = new AGDSAlgorithm(SourceSet.Wine);
        setupCallbacks();
        algorithm.run();
    }

    private void setupCallbacks() {
        algorithm.graphHandler = new GraphCallbacks() {
            @Override
            public void onEngineCreated(AgdsEngine engine) {
                Utils.log("engine created");
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
