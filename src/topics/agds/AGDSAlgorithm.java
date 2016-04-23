package topics.agds;

import common.DataReader;
import common.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class AGDSAlgorithm {

    private DataReader<Item> prepareReader() {
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
        new AGDSAlgorithm().run();
    }

    public void run() {
        DataReader<Item> reader = prepareReader();
        List<Item> dataSet = reader.read(DataReader.IRIS_DATA);
        List<String> classNames = getClassNames(reader.getFirstLine());
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

        AGDSGraphEngine engine = AGDSGraphEngine.initWith(classNames,dataSet)
                .fill()
                .printSetosas()
                .printVersicolors()
                .printVirginicas();


    }

    private List<String> getClassNames(String firstLine) {
        String[] _classes = firstLine.split("\\s");
        List<String> classes = new ArrayList<>();
        classes.addAll(Arrays.asList(_classes));
        return classes;
    }
}
