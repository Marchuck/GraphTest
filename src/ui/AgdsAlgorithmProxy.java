package ui;

import common.Item;
import common.Log;
import some_graphs.GraphMerger;
import topics.agds.GenericAgdsEngine;
import ui.agds.tabs.classify.ClassifyItem;
import ui.connector.ResultCallback;

import java.util.List;

/**
 * @author Lukasz
 * @since 14.05.2016.
 */
public class AgdsAlgorithmProxy {

    private GraphMerger graphMerger;

    public AgdsAlgorithmProxy(GraphMerger graphMerger) {
        this.graphMerger = graphMerger;
    }

    public static void onItemToClassify(GenericAgdsEngine engine, List<ClassifyItem> items, double threshold,
                                        ResultCallback<String> resultCallback) {
        double[][] doubles = new double[4][items.size()];
        for (int j = 0; j < items.size(); j++) {

            doubles[j] = items.get(j).asDoubles();
        }
        engine.classifyNodesSimilarToMany(threshold, doubles,resultCallback);

    }

    public void onItemToClassify(String text0, String text1, String text2, String text3) {
        Log.d("onItemToClassify");
        Double d0 = Double.parseDouble(text0);
        Double d1 = Double.parseDouble(text1);
        Double d2 = Double.parseDouble(text2);
        Double d3 = Double.parseDouble(text3);
        System.out.println("Item to classify: " + d0 + "," + d1 + "," + d2 + "," + d3);
        graphMerger.getAgds().markNodesSimilarTo(5, new Item(new double[]{d0, d1, d2, d3}));
    }
}
