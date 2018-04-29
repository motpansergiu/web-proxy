package dvl.srg.web.reactor;

import dvl.srg.web.reactor.eventhandler.IterableEventHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public final class ReactorManager {

    private final ServerSocketChannel server;
    private final Reactor reactor;
    private final IterableEventHandler iterableEventHandler;

    public ReactorManager(final IterableEventHandler iterableEventHandler, final ServerSocketChannel server, final Reactor reactor) {
        this.iterableEventHandler = iterableEventHandler;
        this.server = server;
        this.reactor = reactor;
    }

    public void run() throws IOException {
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);
        reactor.setIterableEventHandler(iterableEventHandler);
        reactor.run();
    }
}
