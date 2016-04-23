package topics.data_mining;

import common.DataReader;
import common.OrderedPowerSet;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class CombinationProvider<T> {
    private OrderedPowerSet<T> orderedPowerSet;

    public CombinationProvider(List<T> dataSet) {
        this.orderedPowerSet = new OrderedPowerSet<>(dataSet);
    }

    public List<List<T>> provideCombinations(int n) {
        List<List<T>> listOfCombinations = new ArrayList<>();
        List<LinkedHashSet<T>> hashSetList = orderedPowerSet.getPermutationsList(n);
        for (LinkedHashSet<T> s : hashSetList) {
            List<T> combinationOfN = setToArray(s);
            listOfCombinations.add(combinationOfN);
        }
        return listOfCombinations;
    }

    public static <T> List<T> setToArray(Set<T> set) {
        List<T> list = new ArrayList<>();
        for (T element : set) {
            list.add(element);
        }
        return list;
    }
}
