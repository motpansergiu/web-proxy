package dvl.srg.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.util.Objects.requireNonNull;

public final class PropertyFileLoader {

    private static final String CASSANDRA_HOST = "cassandra.host";
    private static final String CASSANDRA_PORT = "cassandra.port";
    private static final String CASSANDRA_REPLICATIONSTRATEGY = "cassandra.replicationstrategy";
    private static final String CASSANDRA_NUMBEROFREPLICAS = "cassandra.numberofreplicas";
    private static final String CASSANDRA_KEYSPACE_NAME = "cassandra.keyspace.name";

    private static final String APPLICATION_SERVER_PORT = "server.port";

    private PropertyFileLoader() {
    }

    public static CassandraProperties loadCassandraProperties(final String fileURL) throws IOException {
        final Properties appProps = new Properties();
        appProps.load(new FileInputStream(requireNonNull(fileURL)));

        final String host = appProps.getProperty(CASSANDRA_HOST);
        final String port = appProps.getProperty(CASSANDRA_PORT);
        final String replicationSrategy = appProps.getProperty(CASSANDRA_REPLICATIONSTRATEGY);
        final String numberOfReplicas = appProps.getProperty(CASSANDRA_NUMBEROFREPLICAS);
        final String keyspace = appProps.getProperty(CASSANDRA_KEYSPACE_NAME);
        return new CassandraProperties(host, Integer.valueOf(port), replicationSrategy, Integer.valueOf(numberOfReplicas), keyspace);
    }

    public static ApplicationProperties loadApplicationProperties(final String fileURL) throws IOException {
        final Properties appProps = new Properties();
        appProps.load(new FileInputStream(requireNonNull(fileURL)));

        final String serverPort = appProps.getProperty(APPLICATION_SERVER_PORT);

        return new ApplicationProperties(Integer.valueOf(serverPort));
    }
}
