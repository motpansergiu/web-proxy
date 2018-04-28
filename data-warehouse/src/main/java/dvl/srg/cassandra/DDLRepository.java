package dvl.srg.cassandra;

import com.datastax.driver.core.Session;

public interface DDLRepository {

    void createTable(Session session);

    void deleteTable(Session session);

}
