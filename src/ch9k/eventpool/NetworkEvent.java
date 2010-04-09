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

    public void setSource(InetAddress source) {
        this.source = source;
    }

    /**
     * Get the IP this Event was sent to
     * @return target
     */
    public InetAddress getTarget() {
        return target;
    }
}
