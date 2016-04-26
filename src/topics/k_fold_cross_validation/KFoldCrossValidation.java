package topics.k_fold_cross_validation;

import common.DataReader;
import common.Item;
import common.Log;
import common.MPair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class KFoldCrossValidation {
    public static final String TAG = KFoldCrossValidation.class.getSimpleName();

    private DataReader<Item> prepareReader() {
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newDottedItemRow(line);
            }
        };

        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        return dataReader.skipFirstLine();
    }

    public static void main(String[] args) {
        new KFoldCrossValidation().run();
    }

    public void run() {
        //prepare dataSet
        List<Item> dataSet = prepareReader().read("randomizer.txt");
        if (!DataReader.dataSetOk(dataSet)) throw new NullPointerException("Data set empty");

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

    private void tenthCrossValidation(List<Item> dataSet) {
        //create candidate
        Item candidate = new Item(new float[]{2, 6, 12, 55});
        //create classifier
        KNNClassifier classifier = new KNNClassifier()
                .withCandidate(candidate)
                .withDataSet(dataSet)
                .observeCandidates();

        Log.d("tenthCrossValidation (basic)");
        for (int j = 0; j < 10; j++) {
            /**
             * |XX|______________| <- XX is neighbours
             * |__|XX|___________|
             * |_____|XX|________|
             * |________|XX|_____|
             * |___________|XX|__|
             * |______________|XX|
             */
            String newName = classifier.sortBasic().classify(10, j);
            Log.d(TAG, "name: " + newName);
        }
        Log.d("tenthCrossValidation: most significant vote: " + classifier.getMostVotedCandidateName());
        classifier.resetObserving();

        Log.d("\n\ntenthCrossValidation (weights included)");
        classifier.observeCandidates();
        for (int j = 0; j < 10; j++) {

            String newName = classifier.sortEuclides().classify(10, j);
            Log.d(TAG, "name: " + newName);
        }
        Log.d("tenthCrossValidation: most significant vote: " + classifier.getMostVotedCandidateName());
    }


}
