package topics.data_mining;

import common.DataReader;
import common.Log;
import common.MPair;
import topics.data_mining.property.PropertyConfidenceBuilder;
import topics.data_mining.property.PropertyManager;
import topics.data_mining.property.PropertySupportBuilder;
import topics.data_mining.transaction.Transaction;
import topics.data_mining.transaction.TransactionManager;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class DataMining {

    public static final String TAG = DataMining.class.getSimpleName();
    public static final String DATA_MINING = "data_mining.txt";
    public static final String IRIS_DATA = "IrisData.txt";
    public static String FILE_TO_READ = IRIS_DATA;


    public DataMining() {

    }

    public static void main(String[] args) {
        Log.d(TAG, "main ");

        new DataMining().run();
    }

    public void run() {

        //create data set
        List<List<String>> dataSet = prepareReader().read(FILE_TO_READ);

        //create property manager and fill with properties
        PropertyManager propertyManager = PropertyManager.create();
        propertyManager.addPropertiesFrom(dataSet);
        //create transactions for each row
        List<Transaction> transactionSet = TransactionManager.createTransactions(dataSet);
        //class which computes single support
        PropertySupportBuilder builder = PropertyManager.computePropertySupport(propertyManager, transactionSet);

        List<Float> propertiesSupport = builder.getNormalized();

        for (int j = 0; j < propertiesSupport.size(); j++) {
            out.println("property name: " + propertyManager.getProperty(j)
                    + " support: " + propertiesSupport.get(j) + " %");
        }

        // compute confidence
        float threshold = 50f;
        System.out.println("\nCompute all possible confidences with threshold " + threshold + "\n");
        PropertyConfidenceBuilder.with(threshold)
                .singleConditionalConfidence(propertyManager.get(), transactionSet);


        runAprioriAlgorithm(propertyManager.get(), transactionSet);
    }

    private DataReader<List<String>> prepareReader() {
        DataReader.ReadStrategy<List<String>> strategy = new DataReader.ReadStrategy<List<String>>() {
            @Override
            public List<String> createNewRow(String line) {
                return DataReader.newStringListRow(line);
            }
        };
        DataReader<List<String>> dataReader = new DataReader<>(strategy);
        dataReader.skipFirstLine();
        return dataReader;
    }

    private void runAprioriAlgorithm(List<String> properties, List<Transaction> transactions) {
        Log.d("\n\n\n***runAprioriAlgorithm***");

        int frequencyThreshold = 6;

        Apriori apriori = new Apriori(frequencyThreshold)
                .withProperties(properties)
                .withTransactions(transactions)
                .pruneStep()
                .joinStep()
                .printMostFrequentProperties();

//        List<MPair<List<String>, Integer>> mostFreq = apriori.get();
//        for (MPair<List<String>, Integer> pair : mostFreq) {
//
//            if (pair.first.size() > 1) {
//                CombinationProvider<String> combinationProvider = new CombinationProvider<>(pair.first);
//                List<List<String>> allPossibleCombinations = new ArrayList<>();
//                for (int n = 1; n < pair.first.size() - 1; n++) {
//                    allPossibleCombinations.addAll(combinationProvider.provideCombinations(n));
//                }
//
//
//            }
//        }
    }
}
