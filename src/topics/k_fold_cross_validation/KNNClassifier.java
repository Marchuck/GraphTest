package topics.k_fold_cross_validation;

import common.Item;
import common.MPair;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class KNNClassifier {

    private List<Item> dataSet;
    private Item candidate;
    private List<MPair<String, Float>> existingListOfCandidates = new ArrayList<>();

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

    public KNNClassifier sortEuclides() {
        Collections.sort(dataSet, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                double d1 = Item.diffDistance(o1, candidate);
                double d2 = Item.diffDistance(o2, candidate);
                return Double.compare(d1, d2);
            }
        });
        return this;
    }

    public String classify(int k, int n) {
        List<Item> neighbours = XValidationUtils.splitToSubLists(dataSet, k).get(n);
        String candidateName = getCandidateName(neighbours, candidate);
        cache(candidateName);
        return candidateName;
    }

    public String getMostVotedCandidateName() {
        return Voter.getHighestVote(existingListOfCandidates);
    }

    private void cache(String candidateName) {
        if (this.existingListOfCandidates != null) {
            Voter.appendValues(existingListOfCandidates, candidateName);
        }
    }

    static class WeightWrap {
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
        existingListOfCandidates = null;
        return this;
    }

    public KNNClassifier observeCandidates() {
        existingListOfCandidates = new ArrayList<>();
        return this;
    }
}
