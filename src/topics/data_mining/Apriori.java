package topics.data_mining;

import common.DataReader;
import common.Log;
import common.OrderedPowerSet;
import topics.data_mining.transaction.Transaction;

import java.util.*;

import static java.lang.System.out;

/**
 * @author Lukasz Marczak
 * @since 22.04.16.
 */
public class Apriori {
    static class Pair<FIRST, SECOND> {
        FIRST first;
        SECOND second;

        public Pair(FIRST first, SECOND second) {
            this.first = first;
            this.second = second;
        }
    }

    public static final String TAG = Apriori.class.getSimpleName();
    private List<String> properties;

    private List<Pair<String, Integer>> propertiesAndFrequnecy;

    private List<String> frequentProperties;

    private List<Transaction> transactions;

    public Apriori() {
        propertiesAndFrequnecy = new ArrayList<>();
    }

    public Apriori withProperties(List<String> properties) {
        this.properties = properties;
        for (String property : properties) {
            propertiesAndFrequnecy.add(new Pair<>(property, 0));
        }
        return this;
    }

    public Apriori withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Apriori pruneStep() {
        if (DataReader.isNullOrEmpty(transactions) || DataReader.isNullOrEmpty(properties))
            throw new IllegalStateException("Cannot perform prune step with nullable dataset");
        return fillProperties().sort().extractWith();
    }

    //todo: create logic according to algorithm(stop when minSup is too small in deeper combination)
    public Apriori joinStep() {
        Log.d(TAG, "joinStep ");
        //this method should be invoked after pruneStep()
        if (DataReader.isNullOrEmpty(frequentProperties))
            throw new IllegalStateException("Invoke pruneStep first!");

        //create combination from list of existing frequenties:
        OrderedPowerSet<String> powerSet = new OrderedPowerSet<>(frequentProperties);
        for (int n = 1; n < 8 ; n++) {

            List<LinkedHashSet<String>> hashSetList = powerSet.getPermutationsList(n);
            for (LinkedHashSet<String> s : hashSetList) {
                List<String> combinationOfN = DataReader.setToArray(s);

                int computedSupport = computeSupport(combinationOfN, transactions);
                if (computedSupport >= currentMinSupport) {
                    Log.d("\n");
                    Log.p("| ");
                    for (String val : combinationOfN) Log.p(val + " | ");
                    Log.d("\ncomputed support: " + computedSupport);
                }
            }
        }

        return this;
    }


    int maxx = 10;
    int currentMinSupport;

    private Apriori extractWith() {
        this.frequentProperties = new ArrayList<>();
        for (int j = 0; j < maxx; j++) {
            frequentProperties.add(propertiesAndFrequnecy.get(j).first);
        }
        currentMinSupport = propertiesAndFrequnecy.get(frequentProperties.size() - 1).second;
        Log.d("Current threshold = " + currentMinSupport);

        return this;
    }

    public Apriori fillProperties() {
        for (Transaction transaction : transactions) {
            for (String propertyFromTransaction : transaction.properties) {
                int index = properties.indexOf(propertyFromTransaction);
                if (index != -1) {
                    Pair<String, Integer> toUpdate = propertiesAndFrequnecy.get(index);
                    int previous = propertiesAndFrequnecy.get(index).second;
                    toUpdate.second = previous + 1;
                    propertiesAndFrequnecy.set(index, toUpdate);
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
        // with min to max support order
        final int sgn = ascending == SortOrder.ASCENDING ? 1 : -1;
        Collections.sort(propertiesAndFrequnecy, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                return sgn * Integer.compare(o1.second, o2.second);
            }
        });
        Log.d(TAG, "sort result: ");
        for (Pair<String, Integer> a : propertiesAndFrequnecy)
            out.println(a.first + ", " + a.second);
        return this;
    }


    public static int computeSupport(List<String> selectedProperties,
                                     List<Transaction> transactions) {
        int finalSupport = 0;
        //avoid duplicates here
        List<String> selectedPropertiesSet = DataReader.listToDistinctList(selectedProperties);
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

    public enum SortOrder {
        ASCENDING, DESCENDING
    }
}

