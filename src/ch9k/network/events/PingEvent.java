package ch9k.network.events;

import ch9k.eventpool.NetworkEvent;
import java.net.InetAddress;

/**
 * useless event, only used to send some data over the wire
 */
public class PingEvent extends NetworkEvent {
    public PingEvent(InetAddress target) {
        super(target);
    }
}