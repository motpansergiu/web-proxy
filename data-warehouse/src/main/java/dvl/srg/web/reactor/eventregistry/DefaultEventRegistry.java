package dvl.srg.web.reactor.eventregistry;

import dvl.srg.web.reactor.eventhandler.EventHandler;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultEventRegistry implements EventRegistry {

    private final Map<Integer, EventHandler> registeredHandlers;

    public DefaultEventRegistry() {
        registeredHandlers = new ConcurrentHashMap<>();
    }

    public DefaultEventRegistry(final int nrOfEvents) {
        registeredHandlers = new ConcurrentHashMap<>(nrOfEvents, 1.0f);
    }

    @Override
    public void registerEventHandler(final int eventType, final EventHandler eventHandler) {
        registeredHandlers.put(eventType, eventHandler);
    }

    @Override
    public Optional<EventHandler> findEventHandler(int eventType) {
        return Optional.ofNullable(registeredHandlers.get(eventType));
    }
}
