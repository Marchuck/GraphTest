package common;

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
