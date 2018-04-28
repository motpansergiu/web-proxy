package dvl.srg.web.reactor.datahandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class RequestDataHandler implements Consumer<byte[]> {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataHandler.class);

    @Override
    public void accept(final byte[] buffer) {
        logger.info("Received message from client : " + new String(buffer));
    }
}
