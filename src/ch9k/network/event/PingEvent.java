package ch9k.network.event;

import ch9k.eventpool.NetworkEvent;
import java.net.InetAddress;

/**
 * Simple event used to send some data over the wire
 */
public class PingEvent extends NetworkEvent {
    public PingEvent(InetAddress target) {
        super(target);
    }
}
