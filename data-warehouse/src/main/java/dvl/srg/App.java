package dvl.srg;

import dvl.srg.infrastructure.cassandra.connector.CassandraConnector;
import dvl.srg.infrastructure.cassandra.connector.CassandraConnectorManager;
import dvl.srg.infrastructure.cassandra.repository.DefaultEmployeeRepository;
import dvl.srg.infrastructure.configuration.ApplicationProperties;
import dvl.srg.infrastructure.configuration.CassandraProperties;
import dvl.srg.infrastructure.configuration.PropertyFileLoader;
import dvl.srg.web.reactor.Reactor;
import dvl.srg.web.reactor.ReactorManager;
import dvl.srg.web.reactor.eventhandler.DefaultIterableSelectionKeyHandler;
import dvl.srg.web.reactor.eventhandler.IterableEventHandler;
import dvl.srg.web.reactor.eventregistry.DefaultEventRegistryFactory;
import dvl.srg.web.reactor.eventregistry.EventRegistryFactory;
import dvl.srg.web.socket.ServerSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

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
                final String path = requireNonNull(App.class.getClassLoader().getResource("")).getPath();
                applicationProperties = PropertyFileLoader.loadApplicationProperties(path + APPLICATION_PROPERTIES);
                cassandraProperties = PropertyFileLoader.loadCassandraProperties(path + CASSANDRA_PROPERTIES);
            } catch (IOException e) {
                logger.error("Cannot load application properties file!", e);
            }

            connectorManager = new CassandraConnectorManager(new CassandraConnector(), cassandraProperties);
            connectorManager.setUpCassandraKeyspace();

            try {
                final ServerSocketManager socketManager = new ServerSocketManager(applicationProperties);

                final Reactor reactor = new Reactor(new AtomicBoolean(true));

                final EventRegistryFactory eventRegistryFactory =
                        new DefaultEventRegistryFactory(new DefaultEmployeeRepository(connectorManager.getSession()), reactor.getDemultiplexer());

                final IterableEventHandler iterableEventHandler = new DefaultIterableSelectionKeyHandler(eventRegistryFactory.newEventRegistry());

                final ReactorManager reactorManager = new ReactorManager(iterableEventHandler, socketManager.start(), reactor);

                reactorManager.run();

            } catch (final IOException e) {
                logger.error("Cannot start reactor!", e);
            }
        } finally {
            if (null != connectorManager) {
                connectorManager.close();
            }
        }
    }
}
