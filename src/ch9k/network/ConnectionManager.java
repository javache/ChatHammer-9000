package ch9k.network;

import java.util.Map;
import java.util.HashMap;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.Iterator;

import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.network.events.CouldNotConnectEvent;
import ch9k.network.events.NetworkConnectionLostEvent;

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
    
    private static final Logger LOGGER =
            Logger.getLogger(ConnectionManager.class.getName());
    
    public ConnectionManager() {
        connectionMap = new HashMap<InetAddress,Connection>();
    }
    
    /**
     * Send a NetworkEvent
     * @param networkEvent 
     */
    public void sendEvent(NetworkEvent networkEvent) {
        InetAddress target = networkEvent.getTarget();
        
        LOGGER.info("sending a NetworkEvent to " + target);
        
        // first try to connect to the target
        if (!connectionMap.containsKey(target)) {
            try {
                connectionMap.put(target,new Connection(target));
            } catch (IOException ex) {
                handleNetworkError(target);
            }
        }
        // next try to send it
        try {
            connectionMap.get(target).sendEvent(networkEvent);
        } catch (IOException e) {
            handleNetworkError(target);
        }
    }
    
    /**
     * Disconnect from all connections
     * send a disconnection Event to all the Connections
     */
     public void disconnect() {
         for (Connection conn : connectionMap.values()) {
             conn.close();
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
      * Handle a network error
      * @param target The InetAddress which failed
      */
     private void handleNetworkError(InetAddress target) {
         if (checkHeartbeat()) {
             signalOffline(target);
         } else {
             signalGlobalConnectionFailure();
         }
     }
     
     /**
      * check if we are online
      * first it will try to open a Connection to any of it's online contacts
      * if there are no other online
      * it will try to open a connection to google.com
      */
     private boolean checkHeartbeat() {
         boolean online = false;
         // only one connection to test with
         if (connectionMap.values().size() == 1) {
            online = true;
            try {
                new Socket("www.google.com",80);
            } catch (Exception e) {
                online = false;
            }
         } else {
             Iterator<Connection> it = connectionMap.values().iterator();
             while(it.hasNext() && ! online) {
                 Connection conn = it.next();
                 online = conn.hasConnection();
             }
         }
         
         return online;
     }
     
     /**
      * sends an event signalling that target is offline
      */
     private void signalOffline(InetAddress target) {
         EventPool.getInstance().raiseEvent(new CouldNotConnectEvent(this,target));
     }
     
     /**
      * sends an event because we appear to be without internet
      */
     private void signalGlobalConnectionFailure() {
         EventPool.getInstance().raiseEvent(new NetworkConnectionLostEvent(this));
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