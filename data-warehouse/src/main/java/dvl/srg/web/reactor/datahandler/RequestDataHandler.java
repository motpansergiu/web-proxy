package dvl.srg.web.reactor.datahandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestDataHandler implements DataHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestDataHandler.class);

    @Override
    public void handle(final byte[] buffer) {
        logger.info("Received message from client : " + new String(buffer));
    }
}
