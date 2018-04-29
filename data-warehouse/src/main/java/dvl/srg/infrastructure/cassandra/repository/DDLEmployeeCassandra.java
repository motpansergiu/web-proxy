package dvl.srg.infrastructure.cassandra.repository;

import com.datastax.driver.core.Session;
import dvl.srg.infrastructure.cassandra.connector.DDLCassandra;

public final class DDLEmployeeCassandra implements DDLCassandra {

    private static final String TABLE_NAME = "employee";

    @Override
    public void createTable(final Session session) {
        final StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append("(id uuid PRIMARY KEY, ")
                .append("name text,")
                .append("address text);");
        session.execute(sb.toString());
    }

    @Override
    public void deleteTable(final Session session) {
        session.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
