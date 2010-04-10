package ch9k.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch9k.eventpool.NetworkEvent;

public class UnresponsiveTargetEvent extends NetworkEvent {
    public UnresponsiveTargetEvent() throws UnknownHostException {
        super(InetAddress.getByName("google.com"));
    }
}
