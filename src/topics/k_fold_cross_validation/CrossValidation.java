package topics.k_fold_cross_validation;

import common.DataReader;
import common.Item;
import common.Log;
import common.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class CrossValidation {
    public static final String TAG = CrossValidation.class.getSimpleName();

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
        new CrossValidation().run();
    }

    public void run() {
        //prepare dataSet
        List<Item> dataSet = prepareReader().read(DataReader.IRIS_DATA_LARGE);
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");


        dataSet = makeLargeDataset(dataSet);
//        dataSet = makeLargeDataset(dataSet);
//        dataSet = makeLargeDataset(dataSet);
//        dataSet = makeLargeDataset(dataSet);
        Utils.log("dataset size = " + dataSet.size());
        if (dataSet.size() > 2400) dataSet = dataSet.subList(0, 2400);
        tenthCrossValidation(dataSet);
        /**
         * powinno być tak:
         * wybieramy k
         * dzielimy dataSet na K podprzedziałów o liczności L
         * powstaje wówczas para podprzedziałów jeden o liczności
         * size-L (zbiór uczący) oraz L (podzbiór walidacyjny)
         * dla każdego elementu z podzbioru L liczymy odległości wg metryki
         * sprawdzamy czy dla danego elementu nazwa jego klasy zgadza się z klasyfikacją (po metryce)
         * na tej podstawie liczymy % ile się zgadza 1 : 1
         * powtarzamy dla wszystkich podzbiorów
         *
         * porównujemy wyniki dla różnych k i różnej metody doboru podzbiorów
         */

    }

    private List<Item> makeLargeDataset(List<Item> dataSet) {
        Random r = new Random();
        List<Item> out = new ArrayList<>();
        out.addAll(dataSet);
        for (Item i : dataSet) {
            out.add(new Item(fuzzy(i, r), i.name));
        }
        return out;
    }

    private double[] fuzzy(Item i, Random r) {
        return new double[]{
                i.values[0] + r.nextDouble(),
                i.values[1] + r.nextDouble(),
                i.values[2] + r.nextDouble(),
                i.values[3] + r.nextDouble(),
        };
    }

    private void tenthCrossValidation(List<Item> dataSet) {
        //create candidate
        Item candidate = new Item(new double[]{2, 6, 12, 55});
        //create classifier
        KNNClassifier classifier = new KNNClassifier()
                .withCandidate(candidate)
                .withDataSet(dataSet);


        for (int K = 2; K < 15; K++) {
            double[] optimalK = classifier.optimalKResultRandomized(K, K);
            Log.d(TAG, "value for K = : " + K + " : " + String.format("%2.7f", optimalK[0]) + "," + String.format("%2.7f", optimalK[1]));
        }
    }
}
