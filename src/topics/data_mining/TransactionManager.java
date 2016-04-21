/**
 *
 */
package topics.data_mining;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukasz Marczak
 */
public class TransactionManager {
    private TransactionManager() {
    }

    public static List<Transaction> createTransactions(List<List<String>> dataSet) {
        List<Transaction> transactions = new ArrayList<>();
        for (List<String> stringList : dataSet) {
            transactions.add(new Transaction(listToDistinctList(stringList)));
        }
        return transactions;
    }


    public static <T> List<T> listToDistinctList(List<T> list) {
        Set<T> set = new HashSet<>();
        set.addAll(list);
        List<T> distinctList = new ArrayList<>();
        distinctList.addAll(set);
        return distinctList;
    }


}
