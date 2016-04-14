package topics.nearest_k_neighbours;

import common.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NearestCommon {

	private NearestCommon() {
	}
	public static String classify(List<Item> nearest) {
		Set<String> names = new HashSet<>();
		List<Integer> weights = new ArrayList<>();
		for (Item q : nearest) {
			weights.add(0);
			names.add(q.name);
		}
		List<String> uniqueNames = new ArrayList<>();
		uniqueNames.addAll(names);

		for (Item q : nearest) {
			for(int k = 0; k < uniqueNames.size();k++){
				String n = uniqueNames.get(k);
				if(n.equals(q.name)){
					weights.set(k, weights.get(k)+1);
				}
			}
		}
		int indexOfMax = weights.indexOf(Collections.max(weights));
		return uniqueNames.get(indexOfMax);
	}

	public static List<Item> getNeighbours(List<Item> source,
										   int selectedIndex, int count) {
		List<Item> neighbours = new ArrayList<>();
		++count;
		int maxIndex = Math.min(count, source.size());

		if (selectedIndex != -1) {
			int slice = count / 2;
			if (selectedIndex - slice <= 0) {
				for (int j = 0; j < maxIndex; j++) {
					if (j != selectedIndex)
						neighbours.add(source.get(j));
				}
			} else {
				for (int j = selectedIndex - slice; j < selectedIndex - slice
						+ maxIndex; j++) {
					if (j != selectedIndex)
						neighbours.add(source.get(j));
				}
			}
		} else {
			System.err.println("index is " + selectedIndex);
		}
		return neighbours;
	}

}
