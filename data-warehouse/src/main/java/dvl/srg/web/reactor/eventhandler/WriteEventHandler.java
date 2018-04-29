package dvl.srg.web.reactor.eventhandler;

import dvl.srg.domain.model.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public final class WriteEventHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(WriteEventHandler.class);

    private EmployeeRepository employeeRepository;

    private ByteBuffer inputBuffer = ByteBuffer.allocate(8096);

    public WriteEventHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void accept(final SelectionKey handle) throws IOException {
        logger.info("Handle write selection key " + handle.channel());
        SocketChannel socketChannel = (SocketChannel) handle.channel();
//        ByteBuffer inputBuffer = (ByteBuffer) handle.attachment();

        final StringBuilder sb = new StringBuilder();

        employeeRepository.findAll().forEach(e -> sb.append(e.toString()));
        inputBuffer.clear();
        inputBuffer.put(sb.toString().getBytes());
        inputBuffer.flip();

        logger.info("Write data into socket channel ");
        socketChannel.write(inputBuffer);
        socketChannel.close();
    }
}