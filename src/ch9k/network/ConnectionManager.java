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
import javax.swing.AbstractListModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.apache.log4j.Logger;


/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager extends AbstractListModel implements ChangeListener {
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
        logger.info("Sending event " + networkEvent.getClass().getName());

        Connection connection = connectionMap.get(networkEvent.getTarget());
        if(connection == null) {
            connection = new Connection(networkEvent.getTarget(), pool, this);
            addConnection(connection, networkEvent.getTarget());
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
            logger.warn(ex.toString());
        }

        // close all listening connections
        int i = 0;
        for (Connection conn : connectionMap.values()) {
            conn.close();
            i++;
        }
        connectionMap.clear();
        fireContentsChanged(this,0,i-1);
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
            removeConnection(target);
            // send an event signalling that target is offline
            pool.raiseEvent(new CouldNotConnectEvent(target));
        } else {
            // sends an event because we appear to be without network connection
            pool.raiseEvent(new NetworkConnectionLostEvent());
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
                logger.warn(ex.toString());
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

    public int getSize() {
        return connectionMap.values().size();
    }
    
    public Object getElementAt(int index) {
        return connectionMap.values().toArray()[index];
    }
    
    public void stateChanged(ChangeEvent event) {
        logger.info("connection is writing");
        fireContentsChanged(this,0,connectionMap.values().size() - 1);
    }

    private void addConnection(Connection con, InetAddress target) {
        connectionMap.put(target, con);
        con.addChangeListener(this);
        fireContentsChanged(this,0,connectionMap.values().size() - 1);
    }

    private void removeConnection(InetAddress target) {
        connectionMap.remove(target);
        fireContentsChanged(this,0,connectionMap.values().size() - 1);
    }
    
    /**
     * Accepts incoming connections
     */
    private class Listener implements Runnable {
        public void run() {
            /**
             * try to create a ServerSocket
             * we do this in a different try catch
             * because if this one fails
             * our app will be unusable
             */
            try {
                server = new ServerSocket(Connection.DEFAULT_PORT);
            } catch (IOException e) {
                /* TODO: handle this */
                pool.raiseEvent(new NetworkConnectionLostEvent());
            } 
            try {
                // run forever
                logger.info("Started accepting connections!");
                while (!Thread.interrupted()) {
                    Socket client = server.accept();
                    Connection conn = new Connection(client, pool);
                    // TODO worry about synchronisation later
                    addConnection(conn, client.getInetAddress());
                    logger.info("Accepted a new connection! " + client.getInetAddress());
                }
            } catch (IOException ex) {
                // if this fails it would appear as if nothing ever happened
                logger.warn(ex.toString());
            } 
        }
    }
}
