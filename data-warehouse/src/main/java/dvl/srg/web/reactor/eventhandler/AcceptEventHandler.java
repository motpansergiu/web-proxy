package dvl.srg.web.reactor.eventhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public final class AcceptEventHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(AcceptEventHandler.class);

    private Selector demultiplexer;

    public AcceptEventHandler(final Selector demultiplexer) {
        this.demultiplexer = demultiplexer;
    }

    @Override
    public void accept(final SelectionKey handle) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) handle.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();

        if (socketChannel != null) {
            logger.info("Accept a new connection: " + socketChannel.toString());
            socketChannel.configureBlocking(false);
            socketChannel.register(demultiplexer, SelectionKey.OP_READ);
            log(socketChannel);
        }
    }

    private void log(final SocketChannel socketChannel) {
        if (logger.isDebugEnabled()) {
            logger.debug("Registered SelectionKey as READ Mode: " + socketChannel.toString());
        }
    }
}
