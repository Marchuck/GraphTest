package topics.som;

import common.DataReader;
import common.Item;
import common.Log;
import topics.agds.engine.GenericAgdsUtils;

import java.util.List;
import java.util.Random;

/**
 * @author Lukasz
 * @since 09.05.2016.
 */
public class SOM {

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

    /**
     * @param random
     * @param min
     * @param max
     * @return value which is between given values: min < value < max
     */
    private float randomNumber(Random random, float min, float max) {
        return min + (max - min) * random.nextFloat();
    }

    public void run() {
        DataReader<Item> reader = prepareIrisReader();
        List<Item> dataSet = reader.read(DataReader.IRIS_DATA);
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

        double min = dataSet.get(0).values[0];
        double max = dataSet.get(0).values[0];
        for (Item item : dataSet) {
            for (double f : item.values) {
                min = f < min ? f : min;
                max = f > max ? f : max;
            }
        }
        Log.d("min: " + min);
        Log.d("max: " + max);

        int size = 4;
        Random seed = new Random();
        double[][][] array = new double[size][size][4];
        for (int j = 0; j < array.length; j++) {
            for (int k = 0; k < array[j].length; k++) {
                array[j][k] = new double[]{
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                };
            }
        }
    }

    public static void main(String[] args) {
        new SOM().run();

    }
}
