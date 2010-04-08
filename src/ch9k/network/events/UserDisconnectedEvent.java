package ch9k.network.events;

import java.net.InetAddress;

import ch9k.eventpool.Event;

/**
 * this event will be broadcast by a Connection when the remote end closes
 * its stream
 */
public class UserDisconnectedEvent extends Event {
    
    private InetAddress ip;
    
    public UserDisconnectedEvent(InetAddress ip){
        this.ip = ip;
    }
    
    public Object getSource() {
        return null;
    }
    
    public InetAddress getInetAddress() {
        return ip;
    }
}