package dvl.srg.web.reactor.eventhandler;

import dvl.srg.web.reactor.datahandler.DataHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ReadEventHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(ReadEventHandler.class);

    private Selector demultiplexer;
    private DataHandler dataHandler;
    private ByteBuffer inputBuffer = ByteBuffer.allocate(2048);

    public ReadEventHandler(final Selector demultiplexer, final DataHandler dataHandler) {
        this.demultiplexer = demultiplexer;
        this.dataHandler = dataHandler;
    }

    @Override
    public void accept(final SelectionKey handle) throws IOException {

        final SocketChannel socketChannel = (SocketChannel) handle.channel();

        readDataIntoBuffer(socketChannel);

        handleDataFromRequest();

        socketChannel.register(demultiplexer, SelectionKey.OP_WRITE, inputBuffer);
    }

    private void readDataIntoBuffer(final SocketChannel socketChannel) throws IOException {
        logger.info("Start reading data into buffer for connection: " + socketChannel);
        socketChannel.read(inputBuffer);
        logger.info("Finished reading: " + socketChannel);
    }

    private void handleDataFromRequest() {
        inputBuffer.flip();

        byte[] buffer = new byte[inputBuffer.limit()];
        inputBuffer.get(buffer);

        dataHandler.handle(buffer);

        inputBuffer.flip();
    }
}
