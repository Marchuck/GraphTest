package ui.agds.tabs.classify;

import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class ClassifyItem {
    public String property0, property1, property2, property3;
    public List<String> properties;

    public ClassifyItem(List<String> properties) {
        this.properties = properties;
    }

    public double[] asDoubles() {
        double[] doubles = new double[properties.size()];

        for (int j = 0; j < properties.size(); j++) {
            doubles[j] = wrap(properties.get(j));
        }
        return doubles;
    }

    private double wrap(String s) {
        return s.isEmpty() ? -1 : Double.parseDouble(s);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append('[');
        if (properties.size() >= 1) {
            for (int j = 0; j < properties.size() - 1; j++) {
                stringBuilder.append(properties.get(j));
            }
            stringBuilder.append(properties.get(properties.size() - 1)).append(']');
        }
        return stringBuilder.toString();
    }
}
