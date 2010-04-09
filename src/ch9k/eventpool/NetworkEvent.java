package ch9k.eventpool;

import java.net.InetAddress;
import java.io.Serializable;


/**
 * Base event-class that can be sent over the network
 * @author Pieter De Baets
 */
public class NetworkEvent extends Event implements Serializable {
    protected InetAddress source;
    protected InetAddress target;
    protected boolean external;

    /**
     * Create a new NetworkEvent
     * @param target destination
     */
    public NetworkEvent(InetAddress target) {
        this.target = target;
        external = false;
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
        external = true;
    }

    /**
     * Checks if this event was received externally
     * @return external
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * Get the IP this Event was sent to
     * @return target
     */
    public InetAddress getTarget() {
        return target;
    }
}
