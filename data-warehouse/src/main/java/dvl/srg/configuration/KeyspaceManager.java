package dvl.srg.configuration;

import com.datastax.driver.core.Session;

public final class KeyspaceManager {

    private final Session session;

    public KeyspaceManager(Session session) {
        this.session = session;
    }

    /**
     * Method used to create any keyspace - schema.
     *
     * @param keyspaceName the name of the schema.
     * @param replicationStrategy the replication strategy.
     * @param numberOfReplicas the number of replicas.
     *
     */
    public void createKeyspace(final String keyspaceName, final String replicationStrategy, final int numberOfReplicas) {
        final StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName)
                .append(" WITH replication = {")
                .append("'class':'")
                .append(replicationStrategy)
                .append("','replication_factor':")
                .append(numberOfReplicas)
                .append("};");
        session.execute(sb.toString());
    }

    public void useKeyspace(final String keyspace) {
        session.execute("USE " + keyspace);
    }

    /**
     * Method used to delete the specified schema.
     * It results in the immediate, irreversible removal of the keyspace,
     * including all tables and data contained in the keyspace.
     *
     * @param keyspaceName the name of the keyspace to delete.
     */
    public void deleteKeyspace(final String keyspaceName) {
        session.execute("DROP KEYSPACE " + keyspaceName);
    }
}
