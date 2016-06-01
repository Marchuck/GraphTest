package topics.agds;

import agds.GraphDrawer;
import com.sun.istack.internal.Nullable;
import common.DataReader;
import common.Item;
import common.Log;
import some_graphs.GraphVisualiser;
import topics.agds.engine.GenericAgdsEngine;
import topics.agds.nodes.AbstractNode;
import topics.agds.nodes.ValueNode;
import ui.connector.GraphCallbacks;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class AGDSAlgorithm {

    private SourceSet sourceSet;

    public AGDSAlgorithm(SourceSet set) {
        this.sourceSet = set;
    }

    private DataReader<Item> prepareWineReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newWineItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    private DataReader<Item> prepareIrisReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newCommaItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    public static void main(String[] args) {
        new AGDSAlgorithm(SourceSet.Iris).run();
    }

    private GenericAgdsEngine engine;

    private GraphVisualiser graphVisualiser;

    public GraphVisualiser getGraphVisualiser() {
        return graphVisualiser;
    }

    public GenericAgdsEngine getEngine() {
        return engine;
    }

    public void run() {
        DataReader<Item> reader;
        List<Item> dataSet;
        if (sourceSet == SourceSet.Wine) {
            reader = prepareWineReader();
            dataSet = reader.read(DataReader.WINE_DATA);
        } else {
            reader = prepareIrisReader();
            if (sourceSet == SourceSet.Iris_Minimalistic) {
                dataSet = reader.read(DataReader.IRIS_DATA_TWICE_SIMPLIFIED);
            } else if (sourceSet == SourceSet.Iris) {
                dataSet = reader.read(DataReader.IRIS_DATA);
            } else {
                dataSet = reader.read(DataReader.IRIS_DATA_SIMPLIFIED);
            }
        }
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

        List<String> propertyNames = getPropertyNames(reader.getFirstLine());
        List<String> classNames = getClassNames(dataSet);
//        Log.d("properties size = " + propertyNames.size());
//        Log.d("class size = " + classNames.size());
        for (String s : propertyNames) Log.d("next property: " + s);
        for (String s : classNames) Log.d("next className: " + s);
        long timeElapsed = System.currentTimeMillis();
        Log.d("without graph representation: ");
        double max = dataSet.get(0).values[0];
        for (Item it : dataSet) {
            for (double f : it.values)
                if (max < f) max = f;
        }
        Log.d("max = " + max + ", time elapsed: " + (System.currentTimeMillis() - timeElapsed));

        long minTimeElapsed = System.currentTimeMillis();
        Log.d("without graph representation: ");
        double min = dataSet.get(0).values[0];
        for (Item it : dataSet) {
            for (double f : it.values)
                if (min > f) min = f;
        }
        Log.d("min = " + min + ", time elapsed: " + (System.currentTimeMillis() - minTimeElapsed));

        graphVisualiser = new GraphVisualiser("AGDS with Iris data");

        GraphDrawer<AbstractNode> graphDrawer = buildGraphDrawer(graphVisualiser);

        engine = GenericAgdsEngine.initWith(propertyNames, classNames, dataSet)
                .withGraphDrawer(graphDrawer)
                .buildGraph()
                .printMin()
                .printMax()
                .printRecordNodes();
        if (graphHandler != null) graphHandler.onVisualiserCreated(graphVisualiser);
        if (graphHandler != null) graphHandler.onEngineCreated(engine);

        engine.markNodesSimilarToMany(5, engine.randomLeaf(), engine.randomLeaf(), engine.randomLeaf());
        // graphVisualiser.enableLegend();
        //Viewer viewer = graphVisualiser.showGraph();
        // if (graphHandler != null) graphHandler.onGraphCreated(viewer);
    }

    @Nullable
    public GraphCallbacks graphHandler;

    private GraphDrawer<AbstractNode> buildGraphDrawer(final GraphVisualiser graphVisualiser) {
        return new GraphDrawer<AbstractNode>() {
            @Override
            public void drawNode(AbstractNode nodeName) {
                graphVisualiser.shouldAddLabel = !(nodeName instanceof ValueNode);
                graphVisualiser.drawNode(nodeName);
            }

            @Override
            public void drawEdge(AbstractNode nodeA, AbstractNode nodeB) {
                graphVisualiser.drawEdge(nodeA, nodeB);
            }
        };
    }

    private List<String> getPropertyNames(String firstLine) {
        String[] _classes = firstLine.split("\t");
        List<String> classes = new ArrayList<>();
        for (String s : _classes) classes.add(s);
        return classes;
    }

    private List<String> getClassNames(List<Item> items) {
        List<String> classNames = new ArrayList<>();
        for (Item it : items) {
            classNames.add(it.name);
        }
        return DataReader.Utils.toDistinctList(classNames);
    }
}
