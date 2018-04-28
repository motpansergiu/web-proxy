package dvl.srg;

import dvl.srg.cassandra.CassandraConnector;
import dvl.srg.cassandra.CassandraConnectorManager;
import dvl.srg.configuration.ApplicationProperties;
import dvl.srg.configuration.CassandraProperties;
import dvl.srg.configuration.PropertyFileLoader;
import dvl.srg.repository.DefaultEmployeeRepository;
import dvl.srg.web.reactor.Reactor;
import dvl.srg.web.reactor.ReactorManager;
import dvl.srg.web.reactor.eventregistry.DefaultEventRegistryFactory;
import dvl.srg.web.reactor.eventregistry.EventRegistryFactory;
import dvl.srg.web.socket.ServerSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static final String CASSANDRA_PROPERTIES = "cassandra.properties";

    public static void main(String[] args) {
        CassandraConnectorManager connectorManager = null;
        try {
            ApplicationProperties applicationProperties = null;
            CassandraProperties cassandraProperties = null;
            try {
                logger.info("Loading application properties");
                final String path = App.class.getClassLoader().getResource("").getPath();
                applicationProperties = PropertyFileLoader.loadApplicationProperties(path + APPLICATION_PROPERTIES);
                cassandraProperties = PropertyFileLoader.loadCassandraProperties(path + CASSANDRA_PROPERTIES);
            } catch (IOException e) {
                logger.error("Cannot load application properties file!", e);
                System.exit(-1);
            }

            connectorManager = new CassandraConnectorManager(new CassandraConnector(), cassandraProperties);
            connectorManager.setUpCassandraKeyspace();

            try {
                final ServerSocketManager socketManager = new ServerSocketManager(applicationProperties);

                final Reactor reactor = new Reactor(new AtomicBoolean(true));

                final EventRegistryFactory eventRegistryFactory =
                        new DefaultEventRegistryFactory(new DefaultEmployeeRepository(connectorManager.getSession()), reactor.getDemultiplexer());

                final ReactorManager reactorManager = new ReactorManager(eventRegistryFactory.newEventRegistry(), socketManager.start(), reactor);

                reactorManager.run();

            } catch (final IOException e) {
                logger.error("Cannot start reactor!", e);
                System.exit(-1);
            }
        } finally {
            if (null != connectorManager) {
                connectorManager.close();
            }
        }
    }
}
