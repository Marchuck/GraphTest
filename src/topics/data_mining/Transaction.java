/**
 *
 */
package topics.data_mining;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 */
public class Transaction {

    public List<String> properties = new ArrayList<>();

    public Transaction(List<String> properties) {
        this.properties = properties;
    }

}
