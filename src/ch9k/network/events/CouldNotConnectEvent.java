package ch9k.network.events;

import ch9k.eventpool.Event;

/**
 * this event will be broadcasted when the 
 * ConnectionManager failed to create a connection to a certain ip
 */
public class CouldNotConnectEvent extends Event {
    
    public Object getSource() {
        return null;
    }
    
}