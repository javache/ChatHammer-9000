package ch9k.eventpool;

import ch9k.network.ConnectionManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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

    private final List<FilteredListener> listeners = new LinkedList<FilteredListener>();

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
        eventProcessor = new Thread("EventPool-processor") {
            @Override
            public void run() {
                boolean interrupted = false;
                while(!interrupted || eventQueue.size() > 0) {
                    try {
                        broadcastEvent(eventQueue.take());
                    } catch (InterruptedException ex) {
                        interrupted = true;
                    }
                }
            }
        };
        eventProcessor.setDaemon(true);
        eventProcessor.start();
    }

    /**
     * Add a new Event-listener that will listen to a given set of events
     * as defined by the filter.
     * @param listener
     * @param filter
     */
    public void addListener(EventListener listener, EventFilter filter) {
        synchronized(listeners) {
            listeners.add(new FilteredListener(filter, listener));
        }
    }

    /**
     * remove an EventListener
     * @param toRemove
     */
    public void removeListener(EventListener toRemove) {
        synchronized(listeners) {
            Iterator<FilteredListener> it = listeners.iterator();
            while(it.hasNext()) {
                FilteredListener filtered = it.next();
                if(filtered == toRemove) {
                    it.remove();
                }
            }
        }
    }

    /**
     * Send an event through the system
     * @param event
     */
    public void raiseEvent(Event event) {
        eventQueue.add(event);
    }

    /**
     * Send an event through the network and the local pool
     * @param networkEvent
     */
    public void raiseNetworkEvent(NetworkEvent networkEvent) {
        network.sendEvent(networkEvent);
        eventQueue.add(networkEvent);
    }

    private void broadcastEvent(Event event) {
        logger.info("Broadcasting " + event.getClass().getName() + " to " + 
                listeners.size() + " listener(s)");

        List<EventListener> toSend = new ArrayList<EventListener>();
        synchronized(listeners) {
            for(FilteredListener pair : listeners) {
                if(pair.filter.accept(event)) {
                    toSend.add(pair.listener);
                }
            }
        }

        for(int i = 0; i < toSend.size(); i++) {
            try {
                toSend.get(i).handleEvent(event);
            } catch(Exception ex) {
                logger.error(ex.toString(), ex);
            }
        }
    }

    /**
     * Close the EventPool after processing all events
     * @throws PoolsClosedException
     */
    public void close() {
        eventProcessor.interrupt();
        network.disconnect();
    }

    /**
     * Reset all connections
     */
    public void reset() {
        network.clearConnections();
    }
    
    /**
     * Remove all registered listeners
     * To be used only for testing purposes
     */
    public void clearListeners() {
        synchronized(listeners) {
            listeners.clear();
        }
    }
}
