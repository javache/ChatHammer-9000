package ch9k.network;

import java.util.Map;
import java.util.HashMap;
import java.net.InetAddress;
import java.io.IOException;

import ch9k.eventpool.NetworkEvent;

/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager {

    private Map<InetAddress,Connection> connectionMap;
    
    public ConnectionManager() {
        connectionMap = new HashMap<InetAddress,Connection>();
    }
    
    /**
     * Send a NetworkEvent
     * @param networkEvent 
     */
    public void sendEvent(NetworkEvent networkEvent) {
        InetAddress target = networkEvent.getTarget();
        
        if (!connectionMap.containsKey(target)) {
            try {
                connectionMap.put(target,new Connection(target));
            } catch(IOException e) {
                System.out.println(target);
                System.out.println(e);
            }
        }
        
        connectionMap.get(target).sendEvent(networkEvent);
    }
    
    /**
     * Disconnect from all connections
     */
     public void disconnect() {
         
     }
     
}