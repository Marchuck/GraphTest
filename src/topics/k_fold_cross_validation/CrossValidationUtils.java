package topics.k_fold_cross_validation;

import common.DataReader;
import common.Item;
import common.MutablePair;
import common.Utils;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class CrossValidationUtils {

    public static List<String> getClassNames(List<Item> items) {
        Set<String> set = new HashSet<>();
        for (Item it : items) set.add(it.name);
        return DataReader.Utils.toDistinctList(set);
    }

    public static <T> List<List<T>> splitToSubLists(List<T> source, int k) {
        if (source.size() < k) throw new IllegalStateException("Invalid randomizer: " + source + " < " + k);
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
     * @param listList
     * @param l
     * @return pair of lists: first one is learnSet, second is validationSet
     */
    public static MutablePair<List<Item>, List<Item>> getLearnAndValidPair(List<List<Item>> listList, int l) {
        List<Item> validateSet = listList.get(l);
        List<Item> learnSet = new ArrayList<>();
        learnSet.addAll(flatten(listList));
        learnSet.removeAll(validateSet);
        return new MutablePair<>(learnSet, validateSet);

    }

    private static List<? extends Item> flatten(List<List<Item>> dataSet1) {
        List<Item> items = new ArrayList<>();
        for (List<Item> it : dataSet1) items.addAll(it);
        return items;
    }

    public static MutablePair<List<Item>, List<Item>> createLearnAndValidateSets(List<Item> dataSet, int k, int l) {
        if (l >= k) throw new IllegalStateException("Cannot get " + l + "-th subList from list of " + k + " elements");
        List<Item> validateSet = CrossValidationUtils.splitToSubLists(dataSet, k).get(l);
        List<Item> learnSet = new ArrayList<>();
        learnSet.addAll(dataSet);
        learnSet.removeAll(validateSet);
        return new MutablePair<>(learnSet, validateSet);
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

    public static List<List<Item>> getSlicedSets(List<Item> dataSet, int n) {
        Utils.log("getSlicedSets, n = " + n);
        List<List<Item>> separatedLists = new ArrayList<>();
        List<Item> setosas = new ArrayList<>();
        List<Item> virginicas = new ArrayList<>();
        List<Item> versicolors = new ArrayList<>();
        for (Item it : dataSet) {
            if (it.name.contains("setosa")) {
                setosas.add(it);
            } else if (it.name.contains("versicolor")) {
                versicolors.add(it);
            } else virginicas.add(it);
        }
        int minSize1 = minSize(setosas, virginicas, versicolors);

        for (int s = 0; s < n; s++) separatedLists.add(new ArrayList<Item>());

        for (int index = 0; index < minSize1; ) {
            for (int y = 0; index < minSize1 && y < separatedLists.size(); y++) {
                separatedLists.get(y).add(setosas.get(index));
                separatedLists.get(y).add(virginicas.get(index));
                separatedLists.get(y).add(versicolors.get(index));
                index++;
            }
        }
        for (int s = 0; s < n; s++) Utils.log("separated list size: " + separatedLists.get(s).size());

        return separatedLists;
    }

    public static List<List<Item>> getSlicedRandomizedSets(List<Item> dataSet, int n) {
        Utils.log("getSlicedSets, n = " + n);
        List<List<Item>> separatedLists = new ArrayList<>();
        Collections.shuffle(dataSet);
        for (int s = 0; s < n; s++) separatedLists.add(new ArrayList<Item>());

        for (int index = 0; index < dataSet.size(); ) {
            for (int y = 0; index < dataSet.size() && y < separatedLists.size(); y++) {
                separatedLists.get(y).add(dataSet.get(index));
                index++;
            }
        }
        for (int s = 0; s < n; s++) Utils.log("separated list size: " + separatedLists.get(s).size());

        return separatedLists;
    }

    private static int minSize(List... setosas) {
        List<Integer> elements = new ArrayList<>();
        for (List it : setosas) {
            elements.add(it.size());
        }
        return Collections.min(elements);
    }
}
