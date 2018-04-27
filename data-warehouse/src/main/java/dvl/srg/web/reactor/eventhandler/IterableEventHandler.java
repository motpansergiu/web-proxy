package dvl.srg.web.reactor.eventhandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

public interface IterableEventHandler {

    void accept(final Iterator<SelectionKey> selectionKeyIterator) throws IOException;
}
