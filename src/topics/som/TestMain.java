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

    static List<List<Double>> from(List<Item> dataSet) {
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

        DataReader<Item> reader = prepareIrisReader();
        final List<Item> dataSet = reader.read(DataReader.IRIS_DATA);
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");


        CController c = new CController(4, 4, 4, 4,
                Constants.constNumIterations);
        c.CreateDataSet(from(dataSet));
        c.Train(1).make(new GenericCallback<String>() {
            @Override
            public void call(String s) {
                System.out.println("_"+s);
                System.out.println('\n');
            }
        });

    }
}
