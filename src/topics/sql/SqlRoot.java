package topics.sql;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class SqlRoot {
    public SqlRoot() {
    }

    private List<SqlColumn> columns;

    public SqlRoot addColumns(List<SqlColumn> columns) {
        this.columns = columns;
        return this;
    }

    public List<SqlColumn> getColumns() {
        return columns;
    }
}
