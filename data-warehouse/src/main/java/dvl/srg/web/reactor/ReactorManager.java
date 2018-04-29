package dvl.srg.web.reactor;

import dvl.srg.web.reactor.eventhandler.IterableEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public final class ReactorManager {

    private static final Logger logger = LoggerFactory.getLogger(ReactorManager.class);

    private final ServerSocketChannel server;
    private final Reactor reactor;
    private final IterableEventHandler iterableEventHandler;

    public ReactorManager(final IterableEventHandler iterableEventHandler, final ServerSocketChannel server, final Reactor reactor) {
        this.iterableEventHandler = iterableEventHandler;
        this.server = server;
        this.reactor = reactor;
    }

    public void run() throws IOException {
        logger.info("Run ReactorManager");
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);
        reactor.setIterableEventHandler(iterableEventHandler);
        reactor.run();
    }
}
