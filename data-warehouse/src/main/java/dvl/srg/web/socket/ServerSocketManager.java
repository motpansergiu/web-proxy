package dvl.srg.web.socket;

import dvl.srg.infrastructure.configuration.ApplicationProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public final class ServerSocketManager {

    private final ApplicationProperties applicationProperties;

    public ServerSocketManager(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public ServerSocketChannel start() throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(applicationProperties.getServerPort()));
        server.configureBlocking(false);
        return server;
    }
}
