package select;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import com.sun.istack.internal.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * @author Lukasz Marczak
 * @since 11.04.16.
 */
public class DBMaintainer {

    public static final String DB_FILE_PATH = "baza.mdb";
    public static final String tableName = "Studenci";
    public static final String TAG = DBMaintainer.class.getSimpleName();

    public static void main(String[] args) {
        System.out.println("MAIN()");
        try {
            Database database = DatabaseBuilder.open(new File(DB_FILE_PATH));
            Table table = database.getTable(tableName);
            for (Row row : table) {
                System.out.println(TAG + ": next id =  " + row.getId() + ", ");
            }
        } catch (IOException ex) {
            System.err.println("Error to open db: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void insert(AgdsRecord record) {

    }
}
