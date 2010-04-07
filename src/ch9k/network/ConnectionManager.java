package ch9k.network;

import java.util.Map;
import java.util.HashMap;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.Event;

/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager implements EventListener {

    /**
     * a Map to store all the connections.
     */
    private Map<InetAddress,Connection> connectionMap;
    
    ServerSocket server;
    
    
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
                // TODO: useful exception handling here
            }
        }
        connectionMap.get(target).sendEvent(networkEvent);
    }
    
    /**
     * Disconnect from all connections
     * send a disconnection Event to all the Connections
     */
     public void disconnect() {
         for (Connection conn : connectionMap.values()) {
             // TODO should we send the events?
         }
         try {
             // this will cause the listenerthread to crash, and close
             server.close();
         } catch(Exception e) {}
     }
     
     /**
      * call this method when everything is setup to start accepting incoming events
      */
     public void readyForIncomingConnections() {
         startListenThread();
     }
     
     /**
      * handle a UserDisconnectedEvent
      */
     public void handleEvent(Event ev) {
         
     }
     
     /**
      * private helper class
      * will create Connections from clients
      */
     private class SocketHandler implements Runnable {
         
         /**
          * run forever, whenever a client connects, create a new Connection and
          * add it to the map
          */
         public void run() {
             try {
                 server = new ServerSocket(Connection.DEFAULT_PORT);
                 while (true) {
                     Socket client = server.accept();
                     Connection conn = new Connection(client);
                     // TODO worry about synchronisation later
                     connectionMap.put(client.getInetAddress(),conn);
                 }
             } catch (IOException e) {
                 
             }
         }
         
     }
     
     
     /**
      * this will start a thread that will accept incoming connections
      * once a client connects, create a Connection object with it and add it to the Map
      */
     private void startListenThread() {
         new Thread(new SocketHandler()).start();
     }
     
}