package some_graphs;

import org.graphstream.ui.view.Viewer;
import topics.agds.AGDSAlgorithm;
import topics.agds.GenericAgdsEngine;
import topics.agds.SourceSet;

import javax.swing.*;

/**
 * @author Lukasz Marczak
 * @since 12.04.16.
 * Put together drawing graph and control parameters with buttons and other ui widgets
 */
public class GraphMerger extends JFrame {
    public static final String TAG = GraphMerger.class.getSimpleName();

    private GenericAgdsEngine agdsEngine;
    private GraphVisualiser graphVisualiser;
    private Viewer viewer;


    public GenericAgdsEngine getAgds() {
        return agdsEngine;
    }

    public void setAgds(GenericAgdsEngine agds) {
        this.agdsEngine = agds;
    }

    public synchronized GraphVisualiser getGraphVisualiser() {
        return graphVisualiser;
    }

    public GraphMerger(GenericAgdsEngine engine, GraphVisualiser visualiser) {
        super();
        agdsEngine = engine;
        graphVisualiser = visualiser;
        init();
    }

    private void init() {
        viewer = new Viewer(graphVisualiser.getGraph(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
    }

    public GraphMerger() {
        super();
        AGDSAlgorithm algorithm = new AGDSAlgorithm(SourceSet.Iris);
        algorithm.run();
        agdsEngine = algorithm.getEngine();
        graphVisualiser = algorithm.getGraphVisualiser();
        init();
    }

    public static GraphMerger create(GenericAgdsEngine engine, GraphVisualiser visualiser) {
        return new GraphMerger(engine, visualiser);
    }
}
