package ui.tests;

import agds_core.GraphDrawer;
import common.DataReader;
import common.Item;
import common.Log;
import some_graphs.GraphVisualiser;
import topics.agds.AGDSAlgorithm;
import topics.agds.engine.AgdsEngine;
import topics.agds.nodes.AbstractNode;

import java.util.ArrayList;
import java.util.List;

import static common.DataReader.prepareIrisReader;
import static some_graphs.AGDS_Visualiser.sleep;

/**
 * @author Lukasz
 * @since 03.06.2016.
 */
public class SimulationAnimation extends Object {
    public SimulationAnimation() {
    }

    public static void main(String[] args) {
        new SimulationAnimation().run();
    }

    private void run() {
        DataReader<Item> reader = prepareIrisReader();
        List<Item> dataSet;

        dataSet = reader.read(DataReader.IRIS_DATA_TWICE_SIMPLIFIED);

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

        GraphVisualiser graphVisualiser = new GraphVisualiser("AGDSConstants with Iris data");

        GraphDrawer<AbstractNode> graphDrawer = new SleepyDrawer(graphVisualiser);
        graphVisualiser.showGraph();
        AgdsEngine engine = AgdsEngine.initWith(propertyNames, classNames, dataSet)
                .withGraphDrawer(graphDrawer)
                .buildGraph()
                .printMin()
                .printMax()
                .printRecordNodes();
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

    public static class SleepyDrawer extends AGDSAlgorithm.DefaultDrawer {

        public SleepyDrawer(GraphVisualiser visualiser) {
            super(visualiser);
            forceDraw = true;
        }

        @Override
        public void drawNode(AbstractNode nodeName) {
            super.drawNode(nodeName);
            sleep(330);
        }

        @Override
        public void drawEdge(AbstractNode nodeA, AbstractNode nodeB) {
            super.drawEdge(nodeA, nodeB);
            sleep(330);
        }
    }
}
