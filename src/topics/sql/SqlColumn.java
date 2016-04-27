package topics.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class SqlColumn {
    private String name;
    private List<String> values = new ArrayList<>();

    public String getName(){return name;}
    public SqlColumn(String name) {
        this.name = name;
    }

    public SqlColumn add(String value) {
        values.add(value);
        return this;
    }

    public List<String> equalTo(String query) {
        List<String> output = new ArrayList<>();
        for (String val : values) {
            if (val.equalsIgnoreCase(query)) output.add(val);
        }
        return output;
    }

    public List<String> between(String query1, String query2) {
        List<String> out = new ArrayList<>();
        for (String v : values) {
            if (v.compareTo(query1) == -1 && v.compareTo(query2) == 1) {
                out.add(v);
            }
        }
        return out;
    }
}
