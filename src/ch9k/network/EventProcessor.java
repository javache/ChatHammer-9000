package ch9k.network;

import ch9k.eventpool.NetworkEvent;

public interface EventProcessor {
    /**
     * proces the event, after the processing 
     * it will be send to the EventPool
     */
    void process(NetworkEvent event);
}