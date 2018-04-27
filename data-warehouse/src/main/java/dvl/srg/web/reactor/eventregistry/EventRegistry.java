package dvl.srg.web.reactor.eventregistry;

import dvl.srg.web.reactor.eventhandler.EventHandler;

import java.util.Optional;

public interface EventRegistry {

    void registerEventHandler(int eventType, EventHandler eventHandler);

    Optional<EventHandler> findEventHandler(int eventType);
}
