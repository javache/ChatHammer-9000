package ch9k.eventpool;

import java.io.Serializable;
import java.net.InetAddress;


/**
 * Base event-class that can be sent over the network
 * @author Pieter De Baets
 */
public class NetworkEvent extends Event implements Serializable {
    protected InetAddress source;
    protected InetAddress target;

    /**
     * Create a new NetworkEvent
     * @param target destination
     */
    public NetworkEvent(InetAddress target) {
        this.target = target;
    }

    /**
     * Get the origin of this Event
     * @return source
     */
    @Override
    public InetAddress getSource() {
        return source;
    }

    /**
     * Set the source ip from which this event was received
     * @param source
     */
    public void setSource(InetAddress source) {
        this.source = source;
    }

    /**
     * Checks if this event was received from an external source
     * @return external
     */
    public boolean isExternal() {
        return source != null;
    }

    /**
     * Get the IP this Event was sent to
     * @return target
     */
    public InetAddress getTarget() {
        return target;
    }
}
