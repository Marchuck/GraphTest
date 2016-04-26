package topics.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class Select {
    private SqlRoot root;

    private Map<String, String> queries = new HashMap<>();

    private Select(SqlRoot root) {
        this.root = root;
    }

    public static Select from(SqlRoot root) {
        return new Select(root);
    }

    public Select equalTo(String columnName, String query) {
        queries.put(columnName, query);
        return this;
    }

    public List<String> execute() {
        List<String> out = new ArrayList<>();

        for (SqlColumn column : root.getColumns()){
            if (queries.containsKey(column)) out.add(queries.get(0));
        }
        return out;
    }
}
