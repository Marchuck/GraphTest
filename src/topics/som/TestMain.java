package topics.som;

import common.DataReader;
import common.Item;
import common.Utils;
import ui.connector.GenericCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz
 * @since 13.05.2016.
 */
public class TestMain {

    public static void main(String[] args) {
        new TestMain().make();
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

    private static List<List<Double>> from(List<Item> dataSet) {
        List<List<Double>> irises = new ArrayList<List<Double>>();
        for (Item item : dataSet) {
            List<Double> doubles = new ArrayList<Double>();
            for (double d : item.values) doubles.add(d);
            irises.add(doubles);
        }
        return irises;
    }

    private void make() {
        Utils.log("make()");

        final List<Item> dataSet = prepareIrisReader().read(DataReader.IRIS_DATA);
        List<List<Double>> irises = from(dataSet);

        CController c = new CController();
        c.CreateDataSet(irises);
        c.trainToFinish(new GenericCallback<String>() {
            int id = 0;

            @Override
            public void call(String s) {
                System.out.println((++id) + '\t' + s);
                System.out.println('\n');
            }
        });

    }
}
