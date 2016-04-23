package topics.data_mining.property;

import common.DataReader;
import javafx.util.Pair;
import topics.data_mining.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 21.04.16.
 * Support definition:
 * Possibility of existence that selected transaction contains X sum Y
 *
 */
public class PropertySupportBuilder {
    private boolean computed;
    private PropertyManager propertyManager;
    private List<Float> propertiesSupport;
    private List<Transaction> transactionSet;

    public PropertySupportBuilder(PropertyManager propertyManager, List<Transaction> transactionSet) {
        this.propertyManager = propertyManager;
        this.transactionSet = transactionSet;
    }

    public List<Float> get() {
        return propertiesSupport;
    }

    private boolean notComputed() {
        return !computed;
    }

    public List<Pair<String, Float>> getPropertiesWithSupport() {
        if (notComputed()) compute();
        List<Pair<String, Float>> pairs = new ArrayList<>();
        for (int j = 0; j < propertiesSupport.size(); j++) {
            String nextProperty = propertyManager.getProperty(j);
            Float nextSupport = propertiesSupport.get(j);
            pairs.add(new Pair<>(nextProperty, nextSupport));
        }
        return pairs;
    }

    public List<Float> getNormalized() {
        if (DataReader.Utils.isNullOrEmpty(this.propertiesSupport))
            throw new NullPointerException("Nullable or empty list");

        for (int j = 0; j < propertiesSupport.size(); j++) {
            float oldValue = propertiesSupport.get(j);
            propertiesSupport.set(j, oldValue * 100 / transactionSet.size());
//            System.out.print("property name: "
//                    + propertyManager.getProperty(j)
//                    + ", support = "
//                    + String.format("%.2f",
//                    oldValue * 100 / transactionSet.size()));
//            System.out.println("%");
        }
        return this.propertiesSupport;
    }

    public PropertySupportBuilder compute() {
        propertiesSupport = new ArrayList<>();

        for (int j = 0; j < propertyManager.size(); j++) {
            propertiesSupport.add(0f);
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
        this.computed = true;
        return this;
    }
}
