package ch9k.network;

import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.events.CouldNotConnectEvent;
import ch9k.network.events.NetworkConnectionLostEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager {
    /**
     * Logger, well does what it says
     */
    private static final Logger LOGGER =
            Logger.getLogger(ConnectionManager.class.getName());

    /**
     * a Map to store all the connections.
     */
    private Map<InetAddress, Connection> connectionMap =
            new ConcurrentHashMap<InetAddress, Connection>();
    
    /**
     * Will listen to incoming connections
     */
    private ServerSocket server;
    
    /**
     * The eventpool we should send messages to
     */
    private EventPool pool;

    /**
     * Create a new ConnectionManager
     * @param pool EventPool where received events will be thrown
     */
    public ConnectionManager(EventPool pool) {
        this.pool = pool;
    }

    /**
     * Send a NetworkEvent
     * @param networkEvent 
     */
    public void sendEvent(final NetworkEvent networkEvent) {
        LOGGER.info("Sending event " + networkEvent.getClass().toString());

        Connection connection = connectionMap.get(networkEvent.getTarget());
        if(connection == null) {
            connection = new Connection(networkEvent.getTarget(), pool, this);
            connectionMap.put(networkEvent.getTarget(), connection);
        }
        connection.sendEvent(networkEvent);
    }

    /**
     * Disconnect from all connections
     * send a disconnection Event to all the Connections
     */
    public void disconnect() {
        // stop accepting new connectons
        try {
            if(server != null) {
                server.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString());
        }

        // close all listening connections
        for (Connection conn : connectionMap.values()) {
            conn.close();
        }
        connectionMap.clear();        
    }

    /**
     * Start listening for incoming connections
     */
    public void readyForIncomingConnections() {
        Thread listenThread = new Thread(new Listener());
        listenThread.setDaemon(true);
        listenThread.start();
    }

    /**
     * Handle a network error
     * @param target The InetAddress which failed
     */
    public void handleNetworkError(InetAddress target) {
        if (checkHeartbeat()) {
            // send an event signalling that target is offline
            pool.raiseEvent(new CouldNotConnectEvent(this, target));
        } else {
            // sends an event because we appear to be without network connection
            pool.raiseEvent(new NetworkConnectionLostEvent(this));
        }
    }

    /**
     * Check if we are online
     * first it will try to open a Connection to any of it's online contacts
     * if there are no other online
     * it will try to open a connection to google.com
     */
    private boolean checkHeartbeat() {
        boolean online = false;
        
        // only one connection to test with
        if (connectionMap.values().size() <= 1) {
            online = true;
            try {
                new Socket("www.google.com", 80);
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, ex.toString());
                online = false;
            }
        } else {
            Iterator<Connection> it = connectionMap.values().iterator();
            while (it.hasNext() && !online) {
                Connection conn = it.next();
                online = conn.hasConnection();
            }
        }

        return online;
    }

    /**
     * Accepts incoming connections
     */
    private class Listener implements Runnable {
        public void run() {
            try {
                server = new ServerSocket(Connection.DEFAULT_PORT);

                // run forever
                LOGGER.info("Started accepting connections!");
                while (!Thread.interrupted()) {
                    Socket client = server.accept();
                    Connection conn = new Connection(client, pool);
                    // TODO worry about synchronisation later
                    connectionMap.put(client.getInetAddress(), conn);
                    LOGGER.info("Accepted a new connection! " + client.getInetAddress());
                }
            } catch (IOException ex) {
                // if this fails it would appear as if nothing ever happened
                LOGGER.log(Level.WARNING, ex.toString());
            }
        }
    }
}
