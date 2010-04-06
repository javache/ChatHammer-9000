package ch9k.network;

import java.util.Map;
import java.util.HashMap;
import java.net.InetAddress;

import ch9k.eventpool.NetworkEvent;

/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager {

    private Map<InetAddress,Connection> connectionMap;
    
    /**
     * Send a NetworkEvent
     * @param networkEvent 
     */
    public void sendEvent(NetworkEvent networkEvent) {
        
    }
    
    /**
     * Disconnect from all connections
     */
     public void disconnect() {
         
     }
     
}