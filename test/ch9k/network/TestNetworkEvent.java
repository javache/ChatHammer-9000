package ch9k.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch9k.eventpool.NetworkEvent;

public class TestNetworkEvent extends NetworkEvent {
    public InetAddress getTarget() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
