package ch9k.eventpool;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributes events across the application
 * @author Pieter De Baets
 */
public class EventPool {
    /**
     * Constructor
     */
    public EventPool() {}

    /**
     * Get the singleton-instance of EventPool
     * @return pool
     */
    public static EventPool getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* Helper-class for singleton, aka Bill Pugh's method */
    private static class SingletonHolder { 
         private static final EventPool INSTANCE = new EventPool();
    }

    private List<FilteredListener> listeners = new ArrayList<FilteredListener>();

    private class FilteredListener {
        public EventFilter filter;
        public EventListener listener;

        public FilteredListener(EventFilter filter, EventListener listener) {
            this.filter = filter;
            this.listener = listener;
        }
    }

    /**
     * Add a new Event-listener that will listen to a given set of events
     * as defined by the filter.
     * @param listener
     * @param filter
     */
    public void addListener(EventListener listener, EventFilter filter) {
        listeners.add(new FilteredListener(filter, listener));
    }

    /**
     * Send an event through the system
     * @param event
     */
    public void raiseEvent(Event event) {
        for(FilteredListener pair : listeners) {
            if(pair.filter.accept(event)) {
                pair.listener.handleEvent(event);
            }
        }
    }

    /**
     * Send an event through the network
     * (will also be broadcast in the local pool?)
     * @param networkEvent
     */
    public void raiseEvent(NetworkEvent networkEvent) {

    }
}
