package topics.data_mining;

import common.DataReader;
import common.Log;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class DataMining {

    public static final String TAG = DataMining.class.getSimpleName();
    public static final String DATA_MINING = "data_mining.txt";


    public DataMining() {

    }

    public static void main(String[] args) {
        Log.d(TAG, "main ");

        new DataMining().run();
    }

    public void run() {

        DataReader.ReadStrategy<List<String>> strategy = new DataReader.ReadStrategy<List<String>>() {
            @Override
            public List<String> createNewRow(String line) {
                return DataReader.newStringListRow(line);
            }
        };
        DataReader<List<String>> dataReader = new DataReader<>(strategy);

        //create data set
        List<List<String>> dataSet = dataReader.read(DATA_MINING);

        //create property manager and fill with properties
        PropertyManager propertyManager = PropertyManager.create();
        propertyManager.addPropertiesFrom(dataSet);
        //create transactions for each row
        List<Transaction> transactionSet = TransactionManager.createTransactions(dataSet);

        List<Float> propertiesSupport2 = PropertyManager
                .calculatePropertiesSupport(propertyManager, transactionSet)
                .getNormalized();
//        out.print("aaaaaaaaaaaaaaaaa");
        for (int j = 0; j < propertiesSupport2.size(); j++) {
            out.println("property name: " + propertyManager.getProperty(j)
                    + " support: " + propertiesSupport2.get(j) + " %");
        }

        List<Float> propertiesSupport = new ArrayList<>();
        List<Float> propertiesConfidence = new ArrayList<>();
        for (int j = 0; j < propertyManager.size(); j++) {
            propertiesSupport.add(0f);
            propertiesConfidence.add(0f);
        }

        for (int j = 0; j < propertyManager.size(); j++) {
            String nextProperty = propertyManager.getProperty(j);
            for (Transaction tr : transactionSet) {
                if (tr.properties.contains(nextProperty)) {
                    float element = propertiesSupport.get(j);
                    propertiesSupport.set(j, element + 1);
                }
            }
        }

        // compute support
        System.out.println("\nCompute support: \n");

        for (int j = 0; j < propertiesSupport.size(); j++) {
            float oldValue = propertiesSupport.get(j);
            propertiesSupport.set(j, oldValue * 100 / transactionSet.size());
            System.out.print("property name: "
                    + propertyManager.getProperty(j)
                    + ", support = "
                    + String.format("%.2f",
                    oldValue * 100 / transactionSet.size()));
            System.out.println("%");
        }

        // compute confidence
        System.out.println("\nCompute all possible confidences\n");
        DataExplorer.runSample();
    }
}
