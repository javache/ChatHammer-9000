package ch9k.network.event;

import ch9k.core.I18n;
import ch9k.eventpool.WarningEvent;
import java.net.InetAddress;

/**
 * this event will be broadcast by a Connection when the remote end closes
 * its stream
 */
public class UserDisconnectedEvent extends WarningEvent {
    private InetAddress ip;
    
    public UserDisconnectedEvent(InetAddress ip){
        super(ip, I18n.get("ch9k.network", "could_not_connect", ip.getHostAddress()));
        this.ip = ip;
    }
    
    @Override
    public InetAddress getSource() {
        return (InetAddress)super.getSource();
    }
}
