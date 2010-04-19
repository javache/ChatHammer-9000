package ch9k.eventpool;

import java.net.InetAddress;

/**
 * an event to send big data over the network. will be handled over a different socket.
 * do NOT trust that this event will be send before others.
 */
public class DataEvent extends NetworkEvent {
    
    public DataEvent(InetAddress target) {
        super(target);
    }
    
}