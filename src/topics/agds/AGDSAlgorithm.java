package topics.agds;

import common.DataReader;
import common.Item;
import common.Log;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class AGDSAlgorithm {

    private DataReader<Item> prepareReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newWineItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    public static void main(String[] args) {
        new AGDSAlgorithm().run();
    }

    public void run() {
        DataReader<Item> reader = prepareReader();
        List<Item> dataSet = reader.read(DataReader.WINE_DATA);
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
            for (float f : it.values)
                if (max < f) max = f;
        }
        Log.d("max = " + max + ", time elapsed: " + (System.currentTimeMillis() - timeElapsed));

        long minTimeElapsed = System.currentTimeMillis();
        Log.d("without graph representation: ");
        double min = dataSet.get(0).values[0];
        for (Item it : dataSet) {
            for (float f : it.values)
                if (min > f) min = f;
        }
        Log.d("min = " + min + ", time elapsed: " + (System.currentTimeMillis() - minTimeElapsed));

        GenericAgdsEngine engine = GenericAgdsEngine.initWith(propertyNames, classNames, dataSet)
                .buildGraph()
                .printMax()
                .printMin();


//        AGDSGraphEngine engine = AGDSGraphEngine.initWith(classNames,dataSet)
//                .buildGraph()
//                .printSetosas()
//                .printVersicolors()
//                .printVirginicas();


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
