package topics.data_mining.property;

import common.DataReader;
import topics.data_mining.PropertyManager;
import topics.data_mining.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 21.04.16.
 */
public class PropertyConfidenceBuilder implements AbstractBuilder {

    private PropertyManager propertyManager;
    private List<Float> propertiesSupport;
    private List<Transaction> transactionSet;

    public PropertyConfidenceBuilder(PropertyManager propertyManager, List<Transaction> transactionSet) {
        this.propertyManager = propertyManager;
        this.transactionSet = transactionSet;
    }

    @Override
    public List<Float> get() {
        return propertiesSupport;
    }

    @Override
    public List<Float> getNormalized() {
        if (DataReader.isNullOrEmpty(this.propertiesSupport))
            throw new NullPointerException("Nullable or empty list");

        for (int j = 0; j < propertiesSupport.size(); j++) {
            float oldValue = propertiesSupport.get(j);
            propertiesSupport.set(j, oldValue * 100 / transactionSet.get(j).properties.size());
            System.out.print("property name: "
//                        + PROPERTY.values()[j].name()
                    + propertyManager.getProperty(j)
                    + ", value = "
                    + String.format("%.2f",
                    oldValue * 100 / transactionSet.size()));
            System.out.println("%");
        }
        return this.propertiesSupport;
    }

    @Override
    public PropertyConfidenceBuilder compute() {
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
        return this;
    }
}
