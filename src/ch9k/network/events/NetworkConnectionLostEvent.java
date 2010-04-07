package ch9k.network.events;

import ch9k.network.ConnectionManager;

public class NetworkConnectionLostEvent extends ConnectionManagerEvent {
    public NetworkConnectionLostEvent(ConnectionManager source) {
        super(source);
    }
}
