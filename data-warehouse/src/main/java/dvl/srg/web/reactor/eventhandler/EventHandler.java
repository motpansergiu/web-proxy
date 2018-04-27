package dvl.srg.web.reactor.eventhandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface EventHandler {

    void accept(SelectionKey selectionKey) throws IOException;
}
