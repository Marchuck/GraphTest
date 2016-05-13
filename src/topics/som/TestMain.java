package topics.som;

import common.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lukasz
 * @since 13.05.2016.
 */
public class TestMain {
    public static void main(String[] args) {


        Map<String, Integer> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("D");
        for (String s : list)
            if (!map.containsKey(s)) {
                map.put(s, 1);
            } else {
                map.put(s, map.get(s) + 1);
            }

        for (String s : map.keySet()) {
            Log.d(s + " : " + map.get(s));
        }
    }
}
