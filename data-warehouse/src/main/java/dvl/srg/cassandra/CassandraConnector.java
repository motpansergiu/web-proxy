package dvl.srg.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Cluster.Builder;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CassandraConnector {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraConnector.class);

    private Cluster cluster;

    private Session session;

    public void connect(final String node, final Integer port) {

        Builder b = Cluster.builder().addContactPoint(node);

        if (port != null) {
            b.withPort(port);
        }

        cluster = b.build();

        logHostData(cluster.getMetadata());

        session = cluster.connect();
    }

    private void logHostData(final Metadata metadata) {
        LOG.info("Cluster name: " + metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            LOG.info("Datacenter: " + host.getDatacenter() + " Host: " + host.getAddress() + " Rack: " + host.getRack());
        }
    }

    public Session getSession() {
        return this.session;
    }

    public void close() {
        session.close();
        cluster.close();
    }
}