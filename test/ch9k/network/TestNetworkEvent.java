package ch9k.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import ch9k.eventpool.NetworkEvent;

public class TestNetworkEvent extends NetworkEvent {
    public TestNetworkEvent() throws UnknownHostException {
        super(InetAddress.getLocalHost());
    }

    public TestNetworkEvent(InetAddress inetAddress) {
        super(inetAddress);
    }

    public TestNetworkEvent(String hostName) throws UnknownHostException {
        super(InetAddress.getByName(hostName));
    }
}
