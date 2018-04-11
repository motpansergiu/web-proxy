package dvl.srg.repository;

import com.datastax.driver.core.Session;

public final class DDLEmployeeRepository {

    private static final String TABLE_NAME = "employee";

    private final Session session;

    public DDLEmployeeRepository(Session session) {
        this.session = session;
    }

    public void createTable() {
        final StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(TABLE_NAME)
                .append("(id uuid PRIMARY KEY, ")
                .append("name text,")
                .append("address text);");
        session.execute(sb.toString());
    }

    public void deleteTable() {
        session.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
}
