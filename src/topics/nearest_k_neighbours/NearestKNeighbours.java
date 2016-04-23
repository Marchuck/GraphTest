package topics.nearest_k_neighbours;

import common.DataReader;
import common.Item;

import java.util.Collections;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class NearestKNeighbours {

    public static int NEAREST_CANDIDATES = 5;

    public static void main(String[] args) {

        //Read strategy how to add object from String line
        DataReader.ReadStrategy<Item> readStrategy = new DataReader.ReadStrategy<Item>() {
            @Override
            public Item createNewRow(String line) {
                return DataReader.newDottedItemRow(line);
            }
        };
        //instantiate generic dataReader with given readStrategy
        DataReader<Item> dataReader = new DataReader<>(readStrategy);
        //create candidate
        Item candidate = new Item(new float[]{1f, 6f, 3f, 4f});
        //get all
        List<Item> list = dataReader.read("data.txt");
        if (list.size() < NEAREST_CANDIDATES || !DataReader.dataSetOk(list))
            DataReader.throwExc("Cannot classify. Not enough elements.");

        list.add(candidate);
        Collections.sort(list);

        int howMuchCandidates = NEAREST_CANDIDATES;//show only 5 nearest candidates
        int index = list.indexOf(candidate);

        System.out.println("Index is " + index);
        List<Item> nearest = NearestCommon.getNeighbours(list, index, howMuchCandidates);
        for (Item q : nearest) {
            System.out.println("neighbour " + q.name);
        }

        System.out.println("Candidate classified to " + NearestCommon.classify(nearest));
    }
}
