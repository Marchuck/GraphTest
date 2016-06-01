package common;

import topics.agds.nodes.RecordNode;

import java.util.List;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class Utils {
    public static boolean isBetween(double low, double high, double value) {
        return low <= value && value <= high;
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public static double[] asVector(RecordNode secondNode) {
        return new double[]{
                (secondNode.getNodes().get(0)).getValue(),
                (secondNode.getNodes().get(1)).getValue(),
                (secondNode.getNodes().get(2)).getValue(),
                (secondNode.getNodes().get(3)).getValue(),
        };
    }

    public interface PrintStrategy<T> {
        String print(T t);
    }

    public static <T> StringBuilder listPrinter(List<T> list, PrintStrategy<T> strategy) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        int j = 0;
        for (; j < list.size() - 1; j++) {
            T node = list.get(j);
            stringBuilder.append(strategy.print(node)).append(',');
        }
        stringBuilder.append(strategy.print(list.get(j)));
        stringBuilder.append(']');
        return stringBuilder;
    }
}
