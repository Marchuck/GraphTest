package topics.som;

import common.DataReader;
import common.Item;
import common.Log;
import common.Utils;
import topics.agds.engine.GenericAgdsUtils;

import java.util.List;
import java.util.Random;

/**
 * @author Lukasz
 * @since 09.05.2016.
 */
public class SOM {
    private static class SomNeuron {
        //coordinates
        public int x;
        public int y;
        public double[] weightVector;
        public double metric;
        public double[] best;

        public SomNeuron(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calculate(double[] X_k) {
            double euclides = euclides(X_k, weightVector);
            metric = euclides;

        }
    }

    private void aaaa() {
        List<Item> dataSet = prepareIrisReader().read(DataReader.IRIS_DATA);
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

        int I = 4;
        int J = 4;
        Random seed = new Random();
        SomNeuron[][] neurons = new SomNeuron[I][J];
        //create initial 2D som map
        for (int i = 0; i < J; i++) {
            for (int j = 0; j < I; j++) {
                neurons[i][j] = new SomNeuron(i, j);
                neurons[i][j].weightVector = randomWeight(seed, min, max);
            }
        }

        for (int i = 0; i < I; i++) {
            for (int j = 0; j < J; j++) {
//                double[]
                for (Item it : dataSet){
                    neurons[i][j].calculate(it.values);

                }

            }
        }

    }

    private double[] randomWeight(Random seed, double min, double max) {
        return new double[]{
                GenericAgdsUtils.produceValueFrom(seed, min, max),
                GenericAgdsUtils.produceValueFrom(seed, min, max),
                GenericAgdsUtils.produceValueFrom(seed, min, max),
                GenericAgdsUtils.produceValueFrom(seed, min, max),
        };
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
        int I = 4;
        int J = 4;
        Random seed = new Random();
        double[][][] w = new double[I][J][4];
        double[] d = new double[dataSet.size()];
        for (int j = 0; j < w.length; j++) {
            for (int k = 0; k < w[j].length; k++) {
                w[j][k] = new double[]{
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                        GenericAgdsUtils.produceValueFrom(seed, min, max),
                };
//                w[j][k] = d[j][k];
            }
        }
        double dMin = Math.sqrt(calculate(dataSet.get(0), I, J, w));
        Utils.log("before Distance min: " + dMin);
        for (int k = 1; k < dataSet.size(); k++) {
            d[k] = Math.sqrt(calculate(dataSet.get(k), I, J, w));
            if (d[k] < dMin) dMin = d[k];
        }
        Utils.log("after Distance min: " + dMin);

    }

    private static double calculate(Item item, int I, int J, double[][][] w) {
        double sum = 0d;
        for (int i = 0; i < I; i++)
            for (int j = 0; j < J; j++) {
                double euclides = euclides(item.values, w[i][j]);
                sum += euclides * euclides;
            }

        return sum;
    }

    private static double euclides(double[] values, double[] doubles) {
        double sum = 0;
        for (int k = 0; k < values.length; k++) {
            double x = values[k] - doubles[k];
            sum += x * x;
        }
        return sum;
    }

    public static void main(String[] args) {
        new SOM().run();

    }
}
