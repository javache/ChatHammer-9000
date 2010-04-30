
package ch9k.network.event;

import ch9k.eventpool.Event;
import ch9k.network.ConnectionManager;

/**
 * Base event class for all events generated by the ConnectionManager
 * @author Pieter De Baets
 */
public abstract class ConnectionManagerEvent extends Event {
    
    @Override
    public Object getSource() {
        return null;
    }
}