package topics.data_mining.property;

import common.Log;
import topics.data_mining.transaction.Transaction;

import java.util.List;

/**
 * @author lukasz
 *         Class responisble to count confidence:
 *         conditional probability p(X|Y) : when transaction contains X, it also contains Y
 */
public class PropertyConfidenceBuilder {
    public static final String TAG = PropertyConfidenceBuilder.class.getSimpleName();
    private double PERCENTAGE_CONFIDENCE_THRESHOLD;

    /**
     * Constructors
     *
     * @param threshold
     */
    private PropertyConfidenceBuilder(double threshold) {
        this.PERCENTAGE_CONFIDENCE_THRESHOLD = threshold;
    }

    public static PropertyConfidenceBuilder with(double confidenceThreshold) {
        if (confidenceThreshold < 0 || confidenceThreshold > 100)
            throw new IllegalArgumentException("Invalid parameter");
        PropertyConfidenceBuilder builder = new PropertyConfidenceBuilder(confidenceThreshold);
        return builder;
    }

    public static PropertyConfidenceBuilder withDefaultThreshold() {
        return new PropertyConfidenceBuilder(50f);//with >50 passes
    }

    public static double compute(String selectedProperty, String otherProperty,
                                 List<Transaction> transactions) {
        int containsSelectedProperty = 0, containsBoth = 0;
        for (Transaction tr : transactions) {
            boolean containsThisProperty = tr.properties.contains(selectedProperty);
            if (containsThisProperty) {
                ++containsSelectedProperty;
                if (tr.properties.contains(otherProperty)) {
                    ++containsBoth;
                }
            }
        }
        return 100 * containsBoth / containsSelectedProperty;
    }

    /**
     * computes conditional
     *
     * @param properties
     * @param transactionSet
     */
    public void singleConditionalConfidence(List<String> properties, List<Transaction> transactionSet) {
        int aboveThreshold = 0, allPossibilities = 0;
        for (String propertySelected : properties) {
            for (String property : properties) {
                //avoid conditions like if kawa then kawa; it'll always compute 100%
                if (!propertySelected.equals(property)) {
                    double confidence = compute(propertySelected, property, transactionSet);
                    if (confidence > PERCENTAGE_CONFIDENCE_THRESHOLD) {
                        System.out.println("if " + propertySelected
                                + " then  " + property + ": possibility = " + confidence + "%");
                        ++aboveThreshold;
                    }
                    ++allPossibilities;
                }
            }
        }
        Log.d(TAG, "all possibilities = " + allPossibilities + ", above treshold = " + aboveThreshold);
        Log.d(TAG, "this is  " + aboveThreshold * 100 / allPossibilities + " % of all.");
    }


    private boolean isNonZero(double confidence) {
        return Double.compare(0.0f, confidence) != 0;
    }
}
