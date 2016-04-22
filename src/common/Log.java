package common;

/**
 * @author lukasz
 * @since 01.04.16.
 */
public class Log {
    public static void d(String TAG, String message) {
        System.out.println(TAG + "," + message);
    }

    public static void d(String message) {
        System.out.println(message);
    }
    public static void p(String message) {
        System.out.print(message);
    }


    public static void e(String TAG, String message) {
        System.err.println(TAG + "," + message);
    }
}
