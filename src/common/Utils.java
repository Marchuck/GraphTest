package common;

/**
 * @author Lukasz
 * @since 26.05.2016.
 */
public class Utils {
    public static boolean isBetween(double low, double high, double value) {
        return low <= value && value <= high;
    }
}
