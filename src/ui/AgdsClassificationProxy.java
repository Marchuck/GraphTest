package ui;

import common.Item;
import common.Log;
import some_graphs.GraphMerger;
import topics.agds.engine.GenericAgdsEngine;
import ui.agds.AgdsApplication;
import ui.agds.tabs.classify.ClassifyItem;
import ui.connector.ResultCallback;

import java.util.List;

/**
 * @author Lukasz
 * @since 14.05.2016.
 */
public class AgdsClassificationProxy {

    private GraphMerger graphMerger;

    @Deprecated
    public AgdsClassificationProxy(GraphMerger graphMerger) {
        this.graphMerger = graphMerger;
    }

    public static void onItemToClassify(GenericAgdsEngine engine, List<ClassifyItem> items, double threshold,
                                        ResultCallback<String> resultCallback) {
        double[][] doubles = new double[items.size()][AgdsApplication.getInstance().numberOfAttributes];
        for (int j = 0; j < items.size(); j++) {
            doubles[j] = items.get(j).asDoubles();
        }
        engine.classifyNodesSimilarToMany(threshold, doubles, resultCallback);
    }

    @Deprecated
    public void onItemToClassify(List<String> inputValues) {
        Log.d("onItemToClassify: " + inputValues.size());
        double[] values = new double[inputValues.size()];
        for (int j = 0; j < inputValues.size(); j++) {
            values[j] = Double.parseDouble(inputValues.get(j));
        }

        //System.out.println("Item to classify: " + d0 + "," + d1 + "," + d2 + "," + d3);
        graphMerger.getAgds().markNodesSimilarTo(5, new Item(values));
    }
}
