package common;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 23.04.16.
 */
public class ListPrinter<T> {

    public interface PrintStrategy<T> {
        String getLeftBrace();

        String getRightBrace();

        String nextItemRow(T t);

        String nextSeparator();
    }

    private PrintStrategy<T> strategy;

    public ListPrinter(PrintStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public String print(List<T> items) {
        if (strategy == null) throw new NullPointerException("Print strategy cannot be null!");

        StringBuilder sb = new StringBuilder();
        sb.append(strategy.getLeftBrace());
        int maxIndex = items.size() - 1;
        for (int j = 0; j < maxIndex - 1; j++) {
            sb.append(strategy.nextItemRow(items.get(j)));
            sb.append(strategy.nextSeparator());
        }
        sb.append(strategy.nextItemRow(items.get(maxIndex)));
        sb.append(strategy.getRightBrace());
        return sb.toString();
    }

    public static abstract class DefaultStrategy<T> implements PrintStrategy<T> {
        @Override
        public String getLeftBrace() {
            return "[";
        }

        @Override
        public String getRightBrace() {
            return "]";
        }

        @Override
        public String nextSeparator() {
            return "|";
        }
    }
}
