package dvl.srg.web.reactor;

import com.datastax.driver.core.Session;
import dvl.srg.repository.DefaultEmployeeRepository;
import dvl.srg.repository.EmployeeRepository;
import dvl.srg.web.reactor.datahandler.RequestDataHandler;
import dvl.srg.web.reactor.eventhandler.AcceptEventHandler;
import dvl.srg.web.reactor.eventhandler.DefaultIterableSelectionKeyHandler;
import dvl.srg.web.reactor.eventhandler.ReadEventHandler;
import dvl.srg.web.reactor.eventhandler.WriteEventHandler;
import dvl.srg.web.reactor.eventregistry.DefaultEventRegistry;
import dvl.srg.web.reactor.eventregistry.EventRegistry;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

public class ReactorManager {

    private final Session session;
    private final ServerSocketChannel server;
    private final Reactor reactor;

    public ReactorManager(final Session session, final ServerSocketChannel server, final Reactor reactor) {
        this.session = session;
        this.server = server;
        this.reactor = reactor;
    }

    public void run() throws IOException {
        reactor.registerChannel(SelectionKey.OP_ACCEPT, server);
        final EventRegistry eventRegistry = eventRegistry(reactor, new DefaultEmployeeRepository(session));
        reactor.setIterableEventHandler(new DefaultIterableSelectionKeyHandler(eventRegistry));
        reactor.run();
    }

    private  EventRegistry eventRegistry(final Reactor reactor, final EmployeeRepository employeeRepository) {
        final EventRegistry eventRegistry = new DefaultEventRegistry(3);
        eventRegistry.registerEventHandler(SelectionKey.OP_ACCEPT, new AcceptEventHandler(reactor.getDemultiplexer()));
        eventRegistry.registerEventHandler(SelectionKey.OP_READ, new ReadEventHandler(reactor.getDemultiplexer(), new RequestDataHandler()));
        eventRegistry.registerEventHandler(SelectionKey.OP_WRITE, new WriteEventHandler(employeeRepository));
        return eventRegistry;
    }
}
