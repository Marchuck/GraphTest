package topics.data_mining.property;

import topics.data_mining.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 21.04.16.
 * Store all unique properties here
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


    public static PropertySupportBuilder computePropertySupport(PropertyManager propertyManager,
                                                                List<Transaction> transactionSet) {
        PropertySupportBuilder builder = new PropertySupportBuilder(propertyManager, transactionSet);
        return builder.compute();
    }


    public static String withoutWhiteCharacters(String s) {
        return s.replaceAll("\\s+", "");
    }

    public List<String> get() {
        return allProperties;
    }
}
