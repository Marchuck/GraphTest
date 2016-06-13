package topics.k_fold_cross_validation;

import common.Item;
import common.MutablePair;
import common.Utils;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class KNNClassifier {

    private List<Item> dataSet;
    private Item candidate;
    private List<MutablePair<String, Float>> existingListOfCandidates = new ArrayList<>();

    public KNNClassifier() {

    }

    public KNNClassifier withDataSet(List<Item> dataSet) {
        this.dataSet = dataSet;
        return this;
    }

    public KNNClassifier withCandidate(Item candidate) {
        this.candidate = candidate;
        return this;
    }

    public KNNClassifier sortBasic() {
        Collections.sort(dataSet);
        return this;
    }

    public KNNClassifier sortWithLInfinity() {
        Collections.sort(dataSet, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double d1 = Item.l_InfinityDistance(o1, candidate);
                double d2 = Item.l_InfinityDistance(o2, candidate);
                return Double.compare(d1, d2);
            }
        });
        return this;
    }

    public KNNClassifier sortWithEuclides() {
        Collections.sort(dataSet, getComparator(candidate));
        return this;
    }

    public static Comparator<Item> getComparator(final Item _candidate) {
        return new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double d1 = Item.diffDistance(o1, _candidate);
                double d2 = Item.diffDistance(o2, _candidate);
                return Double.compare(d1, d2);
            }
        };
    }

    public String classify(int k, int n) {
        List<Item> neighbours = CrossValidationUtils.splitToSubLists(dataSet, k).get(n);
        String candidateName = getCandidateName(neighbours, candidate);
        cache(candidateName);
        return candidateName;
    }

    List<List<Item>> bothSets = null;

    public double[] optimalKResultRandomized(int sn, int k) {
        int n = 10;
        if (bothSets == null) bothSets = CrossValidationUtils.getSlicedRandomizedSets(dataSet, n);
        double meanValue = 0;
        for (int whichOne = 0; whichOne < bothSets.size(); whichOne++) {
            List<Item> validationSet = bothSets.get(whichOne);
            List<Item> learningSet = new ArrayList<>();
            learningSet.addAll(dataSet);
            learningSet.removeAll(validationSet);

            int howMuchSucceeded = 0;
            for (Item it : validationSet) {
                if (it.name.equalsIgnoreCase(getCandidateName(getNeighbours(learningSet, it, k), it)))
                    howMuchSucceeded++;
            }
            Utils.log(howMuchSucceeded + " / " + validationSet.size());
            if (validationSet.size() != 0)
                meanValue += howMuchSucceeded / validationSet.size();
        }
        return new double[]{meanValue, bothSets.size()};
    }

    public double[] optimalKResult(int sn, int k) {
        int n = 10;
        if (bothSets == null) bothSets = CrossValidationUtils.getSlicedSets(dataSet, n);

        double meanValue = 0;
        for (int whichOne = 0; whichOne < bothSets.size(); whichOne++) {
            Utils.log("" + whichOne);
            List<Item> validationSet = bothSets.get(whichOne);
            List<Item> learningSet = new ArrayList<>();
            learningSet.addAll(dataSet);
            learningSet.removeAll(validationSet);

            int howMuchSucceeded = 0;
            for (Item it : validationSet) {
                if (it.name.equalsIgnoreCase(getCandidateName(getNeighbours(learningSet, it, k), it)))
                    howMuchSucceeded++;
            }
            meanValue += howMuchSucceeded / validationSet.size();
        }
        return new double[]{meanValue, bothSets.size()};
    }

    private List<Item> getNeighbours(List<Item> learningSet, Item candidate, int k) {
        candidate.name = "?";
        List<Item> learningList = new ArrayList<>();
        learningList.addAll(learningSet);
        learningList.add(candidate);
        Collections.sort(learningList, getComparator(candidate));
        int index = learningList.indexOf(candidate);
        List<Item> neighbours;
        int half = k / 2;
        if (index - half < 0) {
            neighbours = learningList.subList(0, k);
            neighbours.remove(candidate);
        } else if (index + half > learningList.size()) {
            neighbours = learningList.subList(learningList.size() - k, learningList.size() - 1);
            neighbours.remove(candidate);
        } else {
            neighbours = learningList.subList(index - half, index + half);
            neighbours.remove(candidate);
        }
        return neighbours;
    }

    public String getMostVotedCandidateName() {
        return Voter.getHighestVote(existingListOfCandidates);
    }

    private void cache(String candidateName) {
        if (this.existingListOfCandidates != null) {
            Voter.appendValues(existingListOfCandidates, candidateName);
        }
    }

    private static class WeightWrap {
        private Double distanceSum;
        private Integer amount;

        public WeightWrap(Double distanceSum, Integer amount) {
            this.distanceSum = distanceSum;
            this.amount = amount;
        }

        private double countVoteValue() {
            return (double) distanceSum / amount;
        }

        private WeightWrap updateWith(Double distanceSum) {
            this.distanceSum = +distanceSum;
            amount++;
            return this;
        }
    }

    public String getCandidateName(List<Item> neighbours, Item candidate) {
        String candidateName = "?";
        Map<String, WeightWrap> map = new HashMap<>();
        for (Item neighbour : neighbours) {
            String name = neighbour.name;
            if (map.containsKey(name))
                map.put(name, map.get(name).updateWith(Item.diffDistance(candidate, neighbour)));
            else
                map.put(name, new WeightWrap(Item.diffDistance(candidate, neighbour), 1));
        }

        double lowestVote = 0d;
        for (String objectName : map.keySet()) {

            if (map.get(objectName).countVoteValue() < lowestVote || lowestVote == 0) {
                candidateName = objectName;
                lowestVote = map.get(objectName).countVoteValue();
            }
        }
        return candidateName;
    }

    public KNNClassifier resetObserving() {
        existingListOfCandidates.clear();
        return this;
    }

}
