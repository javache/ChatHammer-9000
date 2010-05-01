package ch9k.network.event;

import ch9k.core.I18n;
import ch9k.eventpool.WarningEvent;
import java.net.InetAddress;

/**
 * Event that will be broadcast when we are unable to connect to a certain ip
 */
public class CouldNotConnectEvent extends WarningEvent {
    public CouldNotConnectEvent(InetAddress ip) {
        super(ip, I18n.get("ch9k.network", "could_not_connect", ip.getHostAddress()));
    }

    @Override
    public InetAddress getSource() {
        return (InetAddress)super.getSource();
    }
}
