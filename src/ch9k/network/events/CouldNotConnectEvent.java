package ch9k.network.events;

import java.net.InetAddress;

import ch9k.network.ConnectionManager;

/**
 * this event will be broadcasted when the 
 * ConnectionManager failed to create a connection to a certain ip
 */
public class CouldNotConnectEvent extends ConnectionManagerEvent {
    
    private InetAddress ip;
    
    public CouldNotConnectEvent(ConnectionManager source,InetAddress ip) {
        super(source);
        this.ip = ip;
    }
    
    public InetAddress getInetAddress() {
        return ip;
    }
    
}
