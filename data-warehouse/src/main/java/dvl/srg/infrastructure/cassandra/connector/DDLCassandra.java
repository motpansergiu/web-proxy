package dvl.srg.infrastructure.cassandra.connector;

import com.datastax.driver.core.Session;

public interface DDLCassandra {

    void createTable(Session session);

    void deleteTable(Session session);

}
