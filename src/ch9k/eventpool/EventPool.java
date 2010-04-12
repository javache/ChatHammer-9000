package ch9k.eventpool;

import ch9k.network.ConnectionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 * Distributes events across the application
 * @author Pieter De Baets
 */
public class EventPool {
    /**
     * Get the application-wide EventPool
     * @return pool
     */
    public static EventPool getAppPool() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(EventPool.class);

    /* Helper-class for singleton, aka Bill Pugh's method */
    private static class SingletonHolder { 
         private static final EventPool INSTANCE = new EventPool(true);
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

    private ConnectionManager network = new ConnectionManager(this);
    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>();
    private Thread eventProcessor;

    /**
     * Constructor
     */
    public EventPool() {
        this(false);
    }

    private EventPool(boolean startListening) {
        // start listening?
        if(startListening) {
            network.readyForIncomingConnections();
        }

        // start the event-processing thread
        eventProcessor = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        broadcastEvent(eventQueue.take());
                    } catch (InterruptedException ex) {
                        // do nothing
                    }
                }
            }
        }, "EventPool-processor");
        eventProcessor.start();
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
     * remove an EventListener
     * @param listener
     */
    public void removeListener(EventListener listener) {
        int i = 0;
        while(i < listeners.size() && listener != listeners.get(i).listener){
            i++;
        }
        if(i < listeners.size()) {
            listeners.remove(i);
        }
    }

    /**
     * Remove all registered listeners
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * Send an event through the system
     * @param event
     */
    public void raiseEvent(Event event) {
        eventQueue.add(event);
    }

    /**
     * Send an event through the network
     * (will also be broadcast in the local pool?)
     * @param networkEvent
     */
    public void raiseEvent(NetworkEvent networkEvent) {
        network.sendEvent(networkEvent);
        eventQueue.add(networkEvent);
    }

    private void broadcastEvent(Event event) {
        logger.info("Sending " + event.getClass().getName());
        for(int i = 0; i < listeners.size(); i++) {
            FilteredListener pair = listeners.get(i);
            if(pair.filter.accept(event)) {
                pair.listener.handleEvent(event);
            }
        }
    }
}
