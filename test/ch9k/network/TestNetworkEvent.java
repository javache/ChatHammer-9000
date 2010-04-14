package ch9k.network;

import ch9k.eventpool.NetworkEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

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
