package ch9k.network.events;

import java.net.InetAddress;

/**
 * Event that will be broadcast when we are unable to connect to a certain ip
 */
public class CouldNotConnectEvent extends ConnectionManagerEvent {
    private InetAddress ip;

    public CouldNotConnectEvent(InetAddress ip) {
        super();
        this.ip = ip;
    }
    
    public InetAddress getInetAddress() {
        return ip;
    }
}
