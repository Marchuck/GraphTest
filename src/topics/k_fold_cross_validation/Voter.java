package topics.k_fold_cross_validation;

import common.Item;
import common.MPair;

import java.util.*;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class Voter {

    private List<List<Item>> data;
    private Item candidate;

    private Voter(Item candidate) {
        this.candidate = candidate;
    }

    public static class Logic {
        List<MPair<String, Float>> votes;

        public Logic(List<MPair<String, Float>> votes) {
            this.votes = votes;
        }

        public void defaultVerdict(String[] verdict) {
            if (verdict.length == 1) {
                Voter.appendValues(votes, verdict[0], 1f);
            } else {
                Voter.appendValues(votes, verdict[0], 0.5f);
                Voter.appendValues(votes, verdict[1], 0.5f);
            }
        }

        public void normalizedVerdict(String verdict) {
            Voter.appendValues(votes, verdict, 1);
        }

        public String verdict() {
            return getHighestVote(votes);
        }
    }

    public Voter withLists(List<List<Item>> data) {
        this.data = data;
        return this;
    }

    public static Voter withCandidate(Item candidate) {

        return new Voter(candidate);
    }

    public String[] voteSingle(List<Item> items) {
        return getSimpleVerdict(items, candidate);
    }

    public String voteNormalized(List<Item> items) {
        return getNormalizedVerdict(items, candidate);
    }

    public String vote() {

        List<MPair<String, Float>> votes = new ArrayList<>();
        for (List<Item> list : data) {

            String[] verdict = getSimpleVerdict(list, candidate);
            if (verdict.length == 1) {
                appendValues(votes, verdict[0], 1f);
            } else {
                appendValues(votes, verdict[0], 0.5f);
                appendValues(votes, verdict[1], 0.5f);
            }
        }
        return getHighestVote(votes);

    }

    public static String getHighestVote(List<MPair<String, Float>> votes) {
        Collections.sort(votes, new Comparator<MPair<String, Float>>() {
            @Override
            public int compare(MPair<String, Float> o1, MPair<String, Float> o2) {
                return -Float.compare(o1.second, o2.second);
            }
        });
        return votes.get(0).first;
    }
    public static void appendValues(List<MPair<String, Float>> votes, String verdict) {
        appendValues(votes, verdict, 1);
    }

    public static void appendValues(List<MPair<String, Float>> votes, String verdict, float value) {
        int index1 = containVote(votes, verdict);
        if (index1 == -1) {
            //add new vote
            votes.add(new MPair<String, Float>(verdict, value));
        } else {
            //updateVote
            MPair<String, Float> current = votes.get(index1);
            current.second += value;
            votes.set(index1, current);
        }
    }

    public static int containVote(List<MPair<String, Float>> votes, String s) {
        for (int j = 0; j > votes.size(); j++) {
            if (votes.get(j).first.equalsIgnoreCase(s)) return j;
        }
        return -1;
    }

    public static String[] getSimpleVerdict(List<Item> list, Item candidate) {
        list.add(candidate);
        Collections.sort(list);
        int index = list.indexOf(candidate);
        if (index == 0) return new String[]{list.get(1).name};
        int lastIndex = list.size() - 1;
        if (index == lastIndex) return new String[]{list.get(lastIndex - 1).name};
        //check neighbours:
        String leftVerdict = list.get(index - 1).name;
        String rightVerdict = list.get(index + 1).name;
        return new String[]{leftVerdict, rightVerdict};
    }

    public static String getNormalizedVerdict(List<Item> list, Item candidate) {
        list.add(candidate);
        Collections.sort(list);
        int index = list.indexOf(candidate);

        List<MPair<String, Float>> votes = new ArrayList<>();
        int listSize = list.size();
        for (int j = 0; j < list.size(); j++) {
            Item consideredItem = list.get(j);
            if (j != index) {
                float value = listSize / Math.abs(j - index);
                appendValues(votes, consideredItem.name, value);
            }
        }
        return getHighestVote(votes);
    }
}
