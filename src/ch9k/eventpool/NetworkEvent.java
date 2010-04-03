package ch9k.eventpool;

import java.net.InetAddress;

/**
 * Base event-class that can be sent over the network
 * @author Pieter De Baets
 */
public class NetworkEvent extends Event {
    private InetAddress source;
    private InetAddress target;

    /**
     * Get the origin of this Event
     * @return source
     */
    public InetAddress getSource() {
        return source;
    }

    /**
     * Get the IP this Event was sent to
     * @return target
     */
    public InetAddress getTarget() {
        return target;
    }
}
