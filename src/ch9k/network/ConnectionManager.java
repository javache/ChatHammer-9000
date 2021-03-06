package ch9k.network;

import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.event.NetworkConnectionLostEvent;
import ch9k.network.event.UserDisconnectedEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;

/**
 * Handles all connections to remote hosts
 * @author nudded
 * @author Pieter De Baets
 */
public class ConnectionManager {
    /**
     * Logger, well does what it says
     */
    private static final Logger logger =
            Logger.getLogger(ConnectionManager.class);

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
        logger.info("Sending event " + networkEvent.getClass().getName() +
                " to " + networkEvent.getTarget());

        Connection connection = connectionMap.get(networkEvent.getTarget());
        if(connection == null) {
            connection = new Connection(networkEvent.getTarget(), pool, this);
            connectionMap.put(networkEvent.getTarget(), connection);
        }
        connection.sendEvent(networkEvent);
    }

    /**
     * clear all connections, starting from scratch
     */
    public void clearConnections() {
        for (Connection connection : connectionMap.values()) {
            connection.close();
        }
        connectionMap.clear();
    }

    /**
     * Disconnect from all connections
     * send a disconnection Event to all the Connections
     */
    public void disconnect() {
        stopServer();
        clearConnections();
    }

    /**
     * stop the ServerSocket
     */
    private void stopServer() {
        // stop accepting new connectons
        try {
            if(server != null) {
                server.close();
            }
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }


    /**
     * Start listening for incoming connections
     */
    public void readyForIncomingConnections() {
        Thread listenThread = new Thread(new Listener(), "Connection-accepter");
        listenThread.setDaemon(true);
        listenThread.start();
    }

    /**
     * Handle a network error
     * @param target The InetAddress which failed
     */
    public void handleNetworkError(InetAddress target) {
        connectionMap.remove(target);
        pool.raiseEvent(new UserDisconnectedEvent(target));
    }

    /**
     * Accepts incoming connections
     */
    private class Listener implements Runnable {
        public void run() {
            // try to create a serversocket
            // (performed in a different try-catch because we need
            // special handling for when this faisl)
            try {
                server = new ServerSocket(Connection.DEFAULT_PORT);
            } catch (IOException e) {
                logger.warn(e.toString());
                pool.raiseEvent(new NetworkConnectionLostEvent());
                return;
            }

            try {
                // run forever
                logger.info("Started accepting connections!");
                while (!Thread.interrupted()) {
                    Socket client = server.accept();
                    Connection conn = connectionMap.get(client.getInetAddress());
                    if (conn != null) {
                        conn.addDataSocket(client);
                        logger.info("Accepted a second connection from " + client.getInetAddress());
                    } else {
                        Connection connection = new Connection(client, pool, ConnectionManager.this);
                        // TODO worry about synchronisation later
                        connectionMap.put(client.getInetAddress(), connection);
                        logger.info("Accepted a new connection from " + client.getInetAddress());
                    }                    
                }
            } catch (IOException ex) {
                // if this fails it would appear as if nothing ever happened
                logger.warn(ex.toString());
                return;
            } 
        }
    }
}
