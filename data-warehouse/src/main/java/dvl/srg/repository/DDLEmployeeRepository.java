package dvl.srg.repository;

import com.datastax.driver.core.Session;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class DDLRepositoryManager {

    private final Session session;

    public DDLRepositoryManager(Session session) {
        this.session = session;
    }

    public void createTable(final String tableName, final String primaryKey, final List<String> columns) {
        requireNonNull(tableName);
        requireNonNull(primaryKey);
        requireNonNull(columns);

        final StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append("(");
        columns.forEach(column -> sb.append(column).append(","));

        sb.append(primaryKey)
                .append(");");

        final String query = sb.toString();
        session.execute(query);
    }
}
