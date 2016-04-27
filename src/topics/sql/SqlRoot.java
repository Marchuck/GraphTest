package topics.sql;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class SqlRoot {
    public SqlRoot() {
    }

    private Set<SqlColumn> columns = new HashSet<>();

    public SqlRoot addColumn(SqlColumn column) {
        columns.add(column);
        return this;
    }

    public Set<SqlColumn> getColumns() {
        return columns;
    }
}
