package dvl.srg.web.reactor;

import dvl.srg.web.reactor.eventhandler.IterableEventHandler;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public final class Reactor {

    private static final Logger logger = LoggerFactory.getLogger(Reactor.class);

    @Setter
    private IterableEventHandler iterableEventHandler;

    private final AtomicBoolean isRunning;

    @Getter
    private final Selector demultiplexer;

    public Reactor(final AtomicBoolean isRunning) throws IOException {
        this.isRunning = requireNonNull(isRunning);
        demultiplexer = Selector.open();
    }

    public void registerChannel(int eventType, SelectableChannel channel) throws IOException {
        channel.register(demultiplexer, eventType);
    }

    public void run() {
        try {
            while (isRunning.get()) {
                logger.info("Waiting for selection keys...");
                demultiplexer.select();
                logger.info("Process existing selected keys");
                final Set<SelectionKey> readyHandles = demultiplexer.selectedKeys();
                iterableEventHandler.accept(readyHandles.iterator());
            }
        } catch (final IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}