package dvl.srg;

import com.datastax.driver.core.Session;
import dvl.srg.configuration.ApplicationProperties;
import dvl.srg.configuration.CassandraConnector;
import dvl.srg.configuration.CassandraProperties;
import dvl.srg.configuration.KeyspaceManager;
import dvl.srg.configuration.PropertyFileLoader;
import dvl.srg.repository.DefaultEmployeeRepository;
import dvl.srg.repository.EmployeeRepository;
import dvl.srg.web.reactor.Reactor;
import dvl.srg.web.reactor.datahandler.RequestDataHandler;
import dvl.srg.web.reactor.eventhandler.AcceptEventHandler;
import dvl.srg.web.reactor.eventhandler.DefaultIterableSelectionKeyHandler;
import dvl.srg.web.reactor.eventhandler.ReadEventHandler;
import dvl.srg.web.reactor.eventhandler.WriteEventHandler;
import dvl.srg.web.reactor.eventregistry.DefaultEventRegistry;
import dvl.srg.web.reactor.eventregistry.EventRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public final class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static final String CASSANDRA_PROPERTIES = "cassandra.properties";

    public static void main(String[] args) {

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

        final CassandraConnector connector = new CassandraConnector();
        connector.connect(cassandraProperties.getHost(), cassandraProperties.getPort());
        final Session session = connector.getSession();

        setUpCassandraKeyspace(cassandraProperties, session);

        final ServerSocketChannel server = setUpAndGetServerSocketChannel(applicationProperties);

        startReactor(session, server);

        connector.close();
    }

    private static ServerSocketChannel setUpAndGetServerSocketChannel(final ApplicationProperties applicationProperties) {
        ServerSocketChannel server = null;
        try {
            server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(applicationProperties.getServerPort()));
            server.configureBlocking(false);

        } catch (IOException e) {
            logger.error("Cannot start server on port !" + applicationProperties.getServerPort(), e);
            System.exit(-1);
        }
        return server;
    }

    private static void startReactor(final Session session, final ServerSocketChannel server) {
        Reactor reactor = null;
        try {
            reactor = new Reactor(new AtomicBoolean(true));
            reactor.registerChannel(SelectionKey.OP_ACCEPT, server);

            final EventRegistry eventRegistry = eventRegistry(reactor, new DefaultEmployeeRepository(session));
            reactor.setIterableEventHandler(new DefaultIterableSelectionKeyHandler(eventRegistry));

        } catch (IOException e) {
            logger.error("Cannot start reactor!", e);
            System.exit(-1);
        }

        reactor.run();
    }

    private static void setUpCassandraKeyspace(final CassandraProperties cassandraProperties, final Session session) {
        final KeyspaceManager sr = new KeyspaceManager(session);
        sr.createKeyspace(cassandraProperties.getKeyspaceName(), cassandraProperties.getReplicationStrategy(), cassandraProperties.getNumberOfReplicas());
        sr.useKeyspace(cassandraProperties.getKeyspaceName());
    }

    private static EventRegistry eventRegistry(final Reactor reactor, final EmployeeRepository employeeRepository) {
        final EventRegistry eventRegistry = new DefaultEventRegistry(3);
        eventRegistry.registerEventHandler(SelectionKey.OP_ACCEPT, new AcceptEventHandler(reactor.getDemultiplexer()));
        eventRegistry.registerEventHandler(SelectionKey.OP_READ, new ReadEventHandler(reactor.getDemultiplexer(), new RequestDataHandler()));
        eventRegistry.registerEventHandler(SelectionKey.OP_WRITE, new WriteEventHandler(employeeRepository));
        return eventRegistry;
    }
}
