package ui.agds.tabs.classify;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class ClassifyItem {
    public String property0, property1, property2, property3;

    public ClassifyItem(String property0, String property1, String property2, String property3) {
        this.property0 = property0;
        this.property1 = property1;
        this.property2 = property2;
        this.property3 = property3;
    }

    public ClassifyItem(String... s) {
        this(s[0], s[1], s[2], s[3]);
    }

    public double[] asDoubles() {
        return new double[]{
                Double.parseDouble(property0),
                Double.parseDouble(property1),
                Double.parseDouble(property2),
                Double.parseDouble(property3),
        };
    }

    @Override
    public String toString() {
        return "[" + property0 + ", " + property1 + ", " + property2 + ", " + property3 + "]";
    }
}
