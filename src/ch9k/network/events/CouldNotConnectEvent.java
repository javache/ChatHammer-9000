package ch9k.network.events;

import ch9k.network.ConnectionManager;
import java.net.InetAddress;

/**
 * this event will be broadcasted when the 
 * ConnectionManager failed to create a connection to a certain ip
 */
public class CouldNotConnectEvent extends ConnectionManagerEvent {
    private InetAddress ip;

    /**
     * 
     * @param source
     * @param ip
     */
    public CouldNotConnectEvent(InetAddress ip) {
        super();
        this.ip = ip;
    }
    
    public InetAddress getInetAddress() {
        return ip;
    }
    
}
