package ch9k.network.event;

import ch9k.core.I18n;
import ch9k.eventpool.WarningEvent;

/**
 * Event thrown when the networkconnection seems to have disappeared
 * @author nudded
 */
public class NetworkConnectionLostEvent extends WarningEvent {
    public NetworkConnectionLostEvent() {
        super(null, I18n.get("ch9k.network", "connection_lost"));
    }
}
