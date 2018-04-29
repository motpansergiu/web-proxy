package dvl.srg.web.socket;

import dvl.srg.infrastructure.configuration.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

import static java.util.Objects.requireNonNull;

public final class ServerSocketManager {

    private static final Logger logger = LoggerFactory.getLogger(ServerSocketManager.class);

    private final ApplicationProperties applicationProperties;

    public ServerSocketManager(final ApplicationProperties applicationProperties) {
        this.applicationProperties = requireNonNull(applicationProperties);
    }

    public ServerSocketChannel start() throws IOException {
        logger.info("Starting server socket...");
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(applicationProperties.getServerPort()));
        server.configureBlocking(false);
        return server;
    }
}
