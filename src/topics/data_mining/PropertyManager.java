package topics.data_mining;

import topics.data_mining.property.AbstractBuilder;
import topics.data_mining.property.PropertyConfidenceBuilder;
import topics.data_mining.property.PropertySupportBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 21.04.16.
 */
public class PropertyManager {

    public static PropertyManager create() {
        return new PropertyManager();
    }

    private PropertyManager() {
    }

    private List<String> allProperties = new ArrayList<>();

    public void addPropertiesFrom(List<List<String>> properties) {
        for (List<String> nextRow : properties)
            for (String property : nextRow)
                if (withoutWhiteCharacters(property).length() > 1 && !this.allProperties.contains(property))
                    this.allProperties.add(property);
    }


    public int size() {
        return allProperties.size();
    }


    public String getProperty(int indexOfProperty) {
        return allProperties.get(indexOfProperty);
    }


    public static AbstractBuilder calculatePropertiesSupport(PropertyManager propertyManager,
                                                             List<Transaction> transactionSet) {
        AbstractBuilder builder = new PropertySupportBuilder(propertyManager, transactionSet);
        return builder.compute();
    }

    public static AbstractBuilder calculatePropertiesConfidence(PropertyManager propertyManager,
                                                                List<Transaction> transactionSet) {
        AbstractBuilder builder = new PropertyConfidenceBuilder(propertyManager, transactionSet);
        return builder;
    }

    public static String withoutWhiteCharacters(String s) {
        return s.replaceAll("\\s+", "");
    }
}
