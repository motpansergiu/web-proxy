package dvl.srg.web.reactor.eventhandler;

import dvl.srg.web.reactor.eventregistry.EventRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Optional;

public class DefaultIterableSelectionKeyHandler implements IterableEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(DefaultIterableSelectionKeyHandler.class);

    private final EventRegistry eventRegistry;

    public DefaultIterableSelectionKeyHandler(final EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void accept(final Iterator<SelectionKey> selectionKeyIterator) throws IOException {
        while (selectionKeyIterator.hasNext()) {

            final SelectionKey selectionKey = selectionKeyIterator.next();
            logger.info("Handle new selection key " + selectionKey);

            if (selectionKey.isAcceptable()) {
                final Optional<EventHandler> eventHandler = eventRegistry.findEventHandler(SelectionKey.OP_ACCEPT);
                if (eventHandler.isPresent()) {
                    eventHandler.get().accept(selectionKey);
                }
            }

            if (selectionKey.isReadable()) {
                final Optional<EventHandler> eventHandler = eventRegistry.findEventHandler(SelectionKey.OP_READ);
                if (eventHandler.isPresent()) {
                    eventHandler.get().accept(selectionKey);
                }
                selectionKeyIterator.remove();
            }

            if (selectionKey.isWritable()) {
                final Optional<EventHandler> eventHandler = eventRegistry.findEventHandler(SelectionKey.OP_WRITE);
                if (eventHandler.isPresent()) {
                    eventHandler.get().accept(selectionKey);
                }
                selectionKeyIterator.remove();
            }
        }
    }
}
