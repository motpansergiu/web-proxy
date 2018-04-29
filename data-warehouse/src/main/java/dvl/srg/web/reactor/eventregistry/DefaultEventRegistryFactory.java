package dvl.srg.web.reactor.eventregistry;

import dvl.srg.domain.model.EmployeeRepository;
import dvl.srg.web.reactor.datahandler.RequestDataHandler;
import dvl.srg.web.reactor.eventhandler.AcceptEventHandler;
import dvl.srg.web.reactor.eventhandler.ReadEventHandler;
import dvl.srg.web.reactor.eventhandler.WriteEventHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

public final class DefaultEventRegistryFactory implements EventRegistryFactory {

    private final EmployeeRepository employeeRepository;
    private final Selector demultiplexer;

    public DefaultEventRegistryFactory(final EmployeeRepository employeeRepository, final Selector demultiplexer) {
        this.employeeRepository = employeeRepository;
        this.demultiplexer = demultiplexer;
    }

    @Override
    public EventRegistry newEventRegistry() {
        final EventRegistry eventRegistry = new DefaultEventRegistry(3);
        eventRegistry.registerEventHandler(SelectionKey.OP_ACCEPT, new AcceptEventHandler(demultiplexer));
        eventRegistry.registerEventHandler(SelectionKey.OP_READ, new ReadEventHandler(demultiplexer, new RequestDataHandler()));
        eventRegistry.registerEventHandler(SelectionKey.OP_WRITE, new WriteEventHandler(employeeRepository));
        return eventRegistry;
    }
}
