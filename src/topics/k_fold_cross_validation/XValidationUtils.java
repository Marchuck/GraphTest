package topics.k_fold_cross_validation;

import common.DataReader;
import common.Item;
import common.MPair;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class XValidationUtils {

    public static List<String> getClassNames(List<Item> items) {
        Set<String> set = new HashSet<>();
        for (Item it : items) set.add(it.name);
        return DataReader.Utils.toDistinctList(set);
    }

    public static <T> List<List<T>> splitToSubLists(List<T> source, int k) {
        if (source.size() < k) throw new IllegalStateException("Invalid data: " + source + " < " + k);
        List<List<T>> outputLists = new ArrayList<>();
        //create n empty arrays
        for (int j = 0; j < k; j++) {
            outputLists.add(new ArrayList<T>());
        }
        for (int i = 0; i < source.size(); i++) {
            T item = source.get(i);
            int index = i % k;
            outputLists.get(index).add(item);
        }
        return outputLists;
    }

    public static List<List<Item>> createSeparated(List<Item> dataSet, List<String> classes) {
        List<List<Item>> lists = new ArrayList<>();
        for (String ignored : classes) lists.add(new ArrayList<Item>());

        for (Item it : dataSet) {
            lists.get(classes.indexOf(it.name)).add(it);
        }
        return lists;
    }

    /**
     *
     * @param listList
     * @param l
     * @return pair of lists: first one is learnSet, second is validationSet
     */
    public static MPair<List<Item>, List<Item>> getLearnAndValidPair(List<List<Item>> listList, int l) {
        List<Item> validateSet = listList.get(l);
        List<Item> learnSet = new ArrayList<>();
        learnSet.addAll(flatten(listList));
        learnSet.removeAll(validateSet);
        return new MPair<>(learnSet, validateSet);

    }

    private static List<? extends Item> flatten(List<List<Item>> dataSet1) {
        List<Item> items = new ArrayList<>();
        for (List<Item> it : dataSet1) items.addAll(it);
        return items;
    }

    public static MPair<List<Item>, List<Item>> createLearnAndValidateSets(List<Item> dataSet, int k, int l) {
        if (l >= k) throw new IllegalStateException("Cannot get " + l + "-th subList from list of " + k + " elements");
        List<Item> validateSet = XValidationUtils.splitToSubLists(dataSet, k).get(l);
        List<Item> learnSet = new ArrayList<>();
        learnSet.addAll(dataSet);
        learnSet.removeAll(validateSet);
        return new MPair<>(learnSet, validateSet);
    }

    public static List<Item> wiseSort(List<Item> items) {
        final List<String> classes = getClassNames(items);
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                int leftIndex = classes.indexOf(o1.name);
                int rightIndex = classes.indexOf(o2.name);
                return 0;
            }
        });
        return items;
    }
}
