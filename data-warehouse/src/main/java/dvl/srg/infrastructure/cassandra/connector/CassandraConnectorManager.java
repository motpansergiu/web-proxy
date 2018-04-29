package dvl.srg.infrastructure.cassandra.connector;

import com.datastax.driver.core.Session;
import dvl.srg.infrastructure.configuration.CassandraProperties;
import dvl.srg.infrastructure.cassandra.keyspace.KeyspaceManager;

import java.util.List;

public final class CassandraConnectorManager {

    private final CassandraConnector connector;
    private final CassandraProperties cassandraProperties;
    private KeyspaceManager keyspaceManager;

    public CassandraConnectorManager(final CassandraConnector connector, final CassandraProperties cassandraProperties) {
        this.connector = connector;
        this.cassandraProperties = cassandraProperties;
        this.connector.connect(cassandraProperties.getHost(), cassandraProperties.getPort());
    }

    public void setUpCassandraKeyspace() {
        final Session session = getSession();
        keyspaceManager = new KeyspaceManager(session);
        keyspaceManager.createKeyspace(cassandraProperties.getKeyspaceName(), cassandraProperties.getReplicationStrategy(), cassandraProperties.getNumberOfReplicas());
        keyspaceManager.useKeyspace(cassandraProperties.getKeyspaceName());
    }

    private void initStorage(final List<DDLCassandra> tables) {
        final Session session = getSession();
        tables.forEach(table -> table.createTable(session));
    }

    public Session getSession() {
        return connector.getSession();
    }

    public void close() {
        connector.close();
    }
}
