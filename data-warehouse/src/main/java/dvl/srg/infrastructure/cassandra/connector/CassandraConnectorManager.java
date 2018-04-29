package dvl.srg.infrastructure.cassandra.connector;

import com.datastax.driver.core.Session;
import dvl.srg.infrastructure.configuration.CassandraProperties;
import dvl.srg.infrastructure.cassandra.keyspace.KeyspaceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Objects.requireNonNull;

public final class CassandraConnectorManager {

    private static final Logger logger = LoggerFactory.getLogger(CassandraConnectorManager.class);

    private final CassandraConnector connector;

    private final CassandraProperties cassandraProperties;

    private KeyspaceManager keyspaceManager;

    public CassandraConnectorManager(final CassandraConnector connector, final CassandraProperties cassandraProperties) {
        this.connector = connector;
        this.cassandraProperties = requireNonNull(cassandraProperties);
        this.connector.connect(cassandraProperties.getHost(), cassandraProperties.getPort());
    }

    public void setUpCassandraKeyspace() {
        logger.info("SetUp Cassandra keyspace");
        final Session session = getSession();
        keyspaceManager = new KeyspaceManager(session);
        keyspaceManager.createKeyspace(cassandraProperties.getKeyspaceName(), cassandraProperties.getReplicationStrategy(), cassandraProperties.getNumberOfReplicas());
        keyspaceManager.useKeyspace(cassandraProperties.getKeyspaceName());
    }

    private void initStorage(final List<DDLCassandra> tables) {
        logger.info("Create cassandra tables");
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
