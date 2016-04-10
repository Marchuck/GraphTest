package utils;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by biela.arek@gmail.com (Arek Biela) on 27.03.2016.
 */
public class Utils {

    public static String getCurrentTimestamp() {
        return new Timestamp(new Date().getTime()).toString();
    }
}
