package ch9k.network.events;

import ch9k.network.ConnectionManager;

/**
 * this event will be broadcasted when the 
 * ConnectionManager failed to create a connection to a certain ip
 */
public class CouldNotConnectEvent extends ConnectionManagerEvent {
    public CouldNotConnectEvent(ConnectionManager source) {
        super(source);
    }
}
