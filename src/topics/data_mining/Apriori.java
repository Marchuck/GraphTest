package topics.data_mining;

import common.*;
import topics.data_mining.transaction.Transaction;

import java.util.*;
import java.util.List;

import static java.lang.System.out;

/**
 * @author Lukasz Marczak
 * @since 22.04.16.
 * Apriori algorithm designed as builder
 */
public class Apriori {
    public static final String TAG = Apriori.class.getSimpleName();
    /**
     * single properties, for example "A", "B", "C" (it depends of input dataSet)
     */
    private List<String> properties;
    /**
     * input data, each transaction has list of properties
     */
    private List<Transaction> transactions;
    /**
     * list of pairs: property with corresponding support
     * (how much this value was present in transactions), for example { "A", 14}
     */
    private List<MPair<String, Integer>> propertiesWithFrequnecy;
    /**
     * store each patterns with corresponding support,
     * for example { {"A", "B", "C"}, 4 }
     * this is the output of Apriori algorithm
     */
    private List<MPair<List<String>, Integer>> mostFrequentProperties;
    /**
     * it is used to create combinations from most frequent properties
     */
    private List<String> singleFrequentProperties;

    /**
     *
     */
    private double THRESHOLD;

    public Apriori(double threshold) {
        this.THRESHOLD = threshold;
        this.propertiesWithFrequnecy = new ArrayList<>();
    }

    public Apriori withProperties(List<String> properties) {
        this.properties = properties;
        for (String property : properties) {
            propertiesWithFrequnecy.add(new MPair<>(property, 0));
        }
        return this;
    }

    public Apriori withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Apriori pruneStep() {
        if (DataReader.Utils.isNullOrEmpty(transactions) || DataReader.Utils.isNullOrEmpty(properties))
            throw new IllegalStateException("Cannot perform prune step with nullable dataset");
        return fillProperties()
                .sort()
                .createMostFrequentSingles(THRESHOLD);
    }


    /**
     * @param threshold value above which properties are considered to be frequent, used
     *                  stronlgy in further step {@link Apriori#joinStep()}
     * @return
     */
    private Apriori createMostFrequentSingles(double threshold) {
        this.mostFrequentProperties = new ArrayList<>();
        this.singleFrequentProperties = new ArrayList<>();
        for (MPair<String, Integer> mPair : propertiesWithFrequnecy) {
            if (mPair.second >= threshold) {
                List<String> list = DataReader.Utils.wrapToList(mPair.first);
                MPair<List<String>, Integer> pair = new MPair<>(list, mPair.second);
                mostFrequentProperties.add(pair);
                singleFrequentProperties.add(mPair.first);
                //    Log.d(TAG, "___adding " + printMostFrequentProperty(pair));
            }
        }

        return this;
    }


    public Apriori fillProperties() {
        for (Transaction transaction : transactions) {
            for (String propertyFromTransaction : transaction.properties) {
                int index = properties.indexOf(propertyFromTransaction);
                if (index != -1) {
                    MPair<String, Integer> toUpdate = propertiesWithFrequnecy.get(index);
                    int previous = propertiesWithFrequnecy.get(index).second;
                    toUpdate.second = previous + 1;
                    propertiesWithFrequnecy.set(index, toUpdate);
                } else {
                    Log.e(TAG, "pruneStep some errors with " + propertyFromTransaction);
                }
            }
        }
        return this;
    }

    public Apriori sort() {
        return sort(SortOrder.DESCENDING);
    }

    public Apriori sort(SortOrder ascending) {
        //create ascending list of property and support pairs
        // with min to max support order or max to min order(DESCENDING)
        final int sgn = ascending == SortOrder.ASCENDING ? 1 : -1;
        Collections.sort(propertiesWithFrequnecy, new Comparator<MPair<String, Integer>>() {
            @Override
            public int compare(MPair<String, Integer> o1, MPair<String, Integer> o2) {
                return sgn * Integer.compare(o1.second, o2.second);
            }
        });
//        for (MPair<String, Integer> a : propertiesWithFrequnecy)
//            out.println(a.first + ", " + a.second);
        return this;
    }


    public static int computeSupport(List<String> selectedProperties,
                                     List<Transaction> transactions) {
        int finalSupport = 0;
        //avoid duplicates here
        List<String> selectedPropertiesSet = DataReader.Utils.toDistinctList(selectedProperties);
        //remember set size
        int itemSetSize = selectedProperties.size();

        for (Transaction transaction : transactions) {
            //iterate over all properties in transactions
            int count = 0;
            for (String aprioriProperty : selectedPropertiesSet) {
                if (transaction.properties.contains(aprioriProperty)) ++count;
            }
            if (count == itemSetSize) {
                //pattern detected, add it to support;
                ++finalSupport;
            }
        }
        return finalSupport;
    }


    public Apriori joinStep() {
        Log.d(TAG, "joinStep ");
        //this method should be invoked after pruneStep()
        if (DataReader.Utils.isNullOrEmpty(singleFrequentProperties))
            throw new IllegalStateException("mostFrequentProperties cannot be empty!");

        //create combination from list of existing frequenties:
        CombinationProvider<String> combProvider = new CombinationProvider<>(singleFrequentProperties);
        //flag which indicates about possibility adding next in n-th iteration at least
        boolean canAddMore = true;
        //single properties are done at prune step while creating singleFrequentProperties,
        // calculate multi dimensional support
        for (int n = 2; canAddMore; n++) {
            canAddMore = false;
            //create combinations for each n
            List<List<String>> combinations = combProvider.provideCombinations(n);
            for (List<String> combination : combinations) {
                if (combination.isEmpty()) continue;
//                Log.d(TAG, "joinStep deal with combination " + printCombination(combination));
                Integer computedSupport = computeSupport(combination, transactions);
                if (computedSupport >= THRESHOLD) {
                    canAddMore = true;
                    mostFrequentProperties.add(new MPair<>(combination, computedSupport));
                }
            }
        }
        return this;
    }

    public List<MPair<List<String>, Integer>> get() {
        return mostFrequentProperties;
    }

    public Apriori printMostFrequentProperties() {
        Log.d(TAG, "\n\n****printMostFrequentProperties***\n\n");
        for (MPair<List<String>, Integer> pair : mostFrequentProperties) {
            Log.d(printMostFrequentProperty(pair));
        }
        return this;
    }

    private String printMostFrequentProperty(MPair<List<String>, Integer> mostFrequentProperty) {
        return "support:  " + mostFrequentProperty.second +
                ", properties: " + printCombination(mostFrequentProperty.first);
    }

    private String printCombination(List<String> first) {

        return new ListPrinter<>(new ListPrinter.DefaultStrategy<String>() {
            @Override
            public String nextItemRow(String s) {
                return s;
            }
        }).print(first);
    }


    public List<String> getSingleMostFreqProperties() {
        return singleFrequentProperties;
    }

    public List<MPair<List<String>, Integer>> findPairsWhichContains(List<String> properties) {
        List<MPair<List<String>, Integer>> matches = new ArrayList<>();
        for (MPair<List<String>, Integer> p : mostFrequentProperties) {
            if (p.first.size() > properties.size() && p.first.containsAll(properties)) matches.add(p);
        }
        return matches;
    }

    private List<MPair<List<String>, Integer>> findPairsWhichContains(List<MPair<List<String>, Integer>> dataSet,
                                                                      String properties) {
        List<MPair<List<String>, Integer>> matches = new ArrayList<>();
        for (MPair<List<String>, Integer> p : dataSet) {
            if (p.first.contains(properties)) matches.add(p);
        }
        return matches;
    }

    public List<MPair<List<String>, Integer>> findPairsWhichContains(String properties) {
        return findPairsWhichContains(mostFrequentProperties, properties);
    }

    public List<String> listsDifference(List<String> list1, List<String> list2) {
        boolean success = list1.removeAll(list2);
        if (success) return list1;
        else return new ArrayList<>();
    }
}

