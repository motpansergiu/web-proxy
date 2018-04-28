package dvl.srg.web.reactor;

import dvl.srg.web.reactor.eventhandler.DefaultIterableSelectionKeyHandler;
import dvl.srg.web.reactor.eventregistry.EventRegistry;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public final class ReactorManager {

    private final ServerSocketChannel server;
    private final Reactor reactor;
    private final EventRegistry eventRegistry;

    public ReactorManager(final EventRegistry eventRegistry, final ServerSocketChannel server, final Reactor reactor) {
        this.eventRegistry = eventRegistry;
        this.server = server;
        this.reactor = reactor;
    }

    public void run() throws IOException {
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);
        reactor.setIterableEventHandler(new DefaultIterableSelectionKeyHandler(eventRegistry));
        reactor.run();
    }


}
