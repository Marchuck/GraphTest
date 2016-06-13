package topics.sql;

import java.util.List;

/**
 * @author Lukasz Marczak
 * @since 26.04.16.
 */
public class SqlRoot {
    private List<SqlColumn> columns;

    public SqlRoot() {
    }

    public SqlRoot addColumns(List<SqlColumn> columns) {
        this.columns = columns;
        return this;
    }

    public List<SqlColumn> getColumns() {
        return columns;
    }
}
