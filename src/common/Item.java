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
}
