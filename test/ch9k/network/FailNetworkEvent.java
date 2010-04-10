package ch9k.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch9k.eventpool.NetworkEvent;

public class FailNetworkEvent extends NetworkEvent {
    public FailNetworkEvent() throws UnknownHostException {
        super(InetAddress.getByName("google.com"));
    }
}
