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
                wrap(property0),
                wrap(property1),
                wrap(property2),
                wrap(property3),
        };
    }

    private double wrap(String s) {
        return s.isEmpty() ? -1 : Double.parseDouble(s);
    }

    @Override
    public String toString() {
        return "[" + property0 + ", " + property1 + ", " + property2 + ", " + property3 + "]";
    }
}
