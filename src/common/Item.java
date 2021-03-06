package common;

import java.util.Arrays;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class Item implements Comparable<Item> {
    public double[] values;
    public String name;

    public Item() {
    }

    public Item(double[] values, String name) {
        this.values = values;
        this.name = name;
    }

    public Item(double[] values) {
        this(values, null);
    }

    @Override
    public int compareTo(Item o) {
        return Double.compare(getMetric(this), getMetric(o));
    }


    public static double getMetric(Item item) {
        double sum = 0f;
        double[] values = item.values;
        for (double value : values) {
            sum += value * value;
        }
        return Math.sqrt(sum);
    }

    /**
     * @param candidate
     * @param object
     * @return Item considered as difference between two Items
     */
    public static Item diff(Item candidate, Item object) {
        double[] values = new double[candidate.values.length];
        for (int j = 0; j < candidate.values.length; j++)
            values[j] = candidate.values[j] - object.values[j];
        return new Item(values);
    }

    @Override
    public String toString() {
        return "name: " + name +
                ", values: " + Arrays.toString(values);
    }

    public static double diffDistance(Item candidate, Item object) {
        return getMetric(diff(candidate, object));
    }

    public static double l_InfinityDistance(Item o1, Item o2) {
        double max = Math.abs(o1.values[0] - o2.values[0]);
        for (double f : o1.values) {
            for (double g : o2.values) {
                double val = Math.abs(f - g);
                if (val > max) max = val;
            }
        }
        return max;
    }
}
