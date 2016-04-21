package topics.data_mining;

import java.util.List;

/**
 * @author lukasz
 */
public class DataExplorer {

    private DataExplorer() {

    }

    public static double calculateConfidence(String selectedProperty,
                                             String otherProperty,
                                             List<Transaction> transactions) {
        int containsSelectedProperty = 0, containsBoth = 0;
        for (Transaction tr : transactions) {
            if (tr.properties.contains(selectedProperty))
                ++containsSelectedProperty;
            if (tr.properties.contains(selectedProperty)
                    && tr.properties.contains(otherProperty))
                ++containsBoth;
        }
        return 100 * containsBoth / containsSelectedProperty;
    }

    public static void calculateConfidence(List<String> ifProperties, List<String> thenProperties,
                                           List<Transaction> transactions) {

    }

    public static void runSample(PropertyManager propertyManager, List<Transaction> transactionSet) {
        for (int j = 0; j < propertyManager.size(); j++) {
            String propertySelected = propertyManager.getProperty(j);

            for (int k = 0; k < propertyManager.size(); k++) {
                String property = propertyManager.getProperty(k);

                if (!propertySelected.equals(property)) {
                    System.out.println("if "
                            + propertySelected
                            + " then  "
                            + property
                            + ": possibility = "
                            + DataExplorer.calculateConfidence(propertySelected,
                            property, transactionSet)
                            + "%");
                }
            }
        }
    }

}
