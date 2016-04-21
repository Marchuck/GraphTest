package topics.cross_validation;

import common.DataReader;
import common.Item;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class CrossValidation {
    public static void main(String[] args) {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newItemRow(line);
            }
        };
        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        List<Item> dataSet = dataReader.read(DataReader.IRIS_DATA);
        if (!dataReader.dataSetOk(dataSet))
            DataReader.throwExc("Not enough elements in data set");

    }

}
