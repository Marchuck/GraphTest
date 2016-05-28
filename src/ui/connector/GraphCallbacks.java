package ui.connector;

import org.graphstream.ui.view.Viewer;
import some_graphs.GraphVisualiser;
import topics.agds.engine.GenericAgdsEngine;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public interface GraphCallbacks {
    void onEngineCreated(GenericAgdsEngine engine);

    void onGraphCreated(Viewer viewer);

    void onVisualiserCreated(GraphVisualiser graphVisualiser);
}
