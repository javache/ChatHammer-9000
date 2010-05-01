package ch9k.eventpool;

import java.net.InetAddress;

/**
 * An event to send big data over the network, will be handled over a different socket.
 * DO NOT trust that this event will be sent before others.
 */
public class DataEvent extends NetworkEvent {
    public DataEvent(InetAddress target) {
        super(target);
    }
}
