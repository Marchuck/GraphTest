package common;

/**
 * @author Lukasz Marczak
 * @since 14.04.16.
 */
public class Item implements Comparable<Item> {
    public float[] values;
    public String name;

    public Item() {
    }

    public Item(float[] values, String name) {
        this.values = values;
        this.name = name;
    }

    public Item(float[] values) {
        this(values, null);
    }

    @Override
    public int compareTo(Item o) {
        return Float.compare(getMetric(this), getMetric(o));
    }


    public static float getMetric(Item item) {
        float sum = 0f;
        float[] values = item.values;
        for (float value : values) {
            sum += value * value;
        }
        return (float) Math.sqrt(sum);
    }

    /**
     *
     * @param candidate
     * @param object
     * @return Item considered as difference between two Items
     */
    public static Item diff(Item candidate, Item object) {
        float[] values = new float[candidate.values.length];
        for (int j = 0; j < candidate.values.length; j++)
            values[j] = candidate.values[j] - object.values[j];
        return new Item(values);
    }

    public static double diffDistance(Item candidate, Item object) {
        return getMetric(diff(candidate, object));
    }

}
