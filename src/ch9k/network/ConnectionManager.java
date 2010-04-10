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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all connections to remote hosts
 * @author nudded
 */
public class ConnectionManager {
    /**
     * a Map to store all the connections.
     */
    private Map<InetAddress, Connection> connectionMap;
    
    /**
     * Will listen to incoming connections
     */
    private ServerSocket server;

    /**
     * Logger, well does what it says
     */
    private static final Logger LOGGER =
            Logger.getLogger(ConnectionManager.class.getName());
    /**
     * a concurrent queue so we can use it in 2 threads
     */
    private LinkedBlockingQueue<NetworkEvent> eventQueue;

    /**
     * a boolean value used to stop dispatch thread
     */
    private Thread dispatcherThread;
    
    /**
     * The eventpool we should send messages to
     */
    private EventPool pool;

    /**
     * Create a new ConnectionManager
     * @param pool EventPool where received events will be thrown
     */
    public ConnectionManager(EventPool pool) {
        eventQueue = new LinkedBlockingQueue<NetworkEvent>();
        connectionMap = new ConcurrentHashMap<InetAddress, Connection>();
        this.pool = pool;

        startDispatcherThread();
    }

    /**
     * Send a NetworkEvent
     * @param networkEvent 
     */
    public void sendEvent(NetworkEvent networkEvent) {
        LOGGER.info("Adding event to queue " + networkEvent.getClass().toString());
        // add to the queue, the dispatch thread will take it from there
        eventQueue.add(networkEvent);
    }

    /**
     * Disconnect from all connections
     * send a disconnection Event to all the Connections
     */
    public void disconnect() {
        // stop accepting new connectons
        try {
            server.close();
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, ex.toString());
        }
        
        // stop sending events
        dispatcherThread.interrupt();

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
        startListenThread();
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
     * sends an event signalling that target is offline
     */
    private void signalOffline(InetAddress target) {
        pool.raiseEvent(new CouldNotConnectEvent(this, target));
    }

    /**
     * sends an event because we appear to be without internet
     */
    private void signalGlobalConnectionFailure() {
        pool.raiseEvent(new NetworkConnectionLostEvent(this));
    }

    private Connection getOrCreateConnection(InetAddress target) {
        if (!connectionMap.containsKey(target)) {
            LOGGER.info("Creating connection to " + target.toString() + " since we don't have one.");
            try {
                connectionMap.put(target, new Connection(target, pool));
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, ex.toString());
                handleNetworkError(target);
                return null;
            }
        }
        return connectionMap.get(target);
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

    /**
     * Starts a thread that will listen for incoming connections
     */
    private void startListenThread() {
        Thread listenThread = new Thread(new Listener());
        listenThread.setDaemon(true);
        listenThread.start();
    }

    /**
     * Sends NetworkEvents available on the networkQueue
     */
    private class Dispatcher implements Runnable {
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    NetworkEvent ev = eventQueue.take();
                    if (ev == null) {
                        continue;
                    }
                    try {
                        Connection conn = getOrCreateConnection(ev.getTarget());
                        if(conn != null) {
                            conn.sendEvent(ev);
                        }
                    } catch (IOException ex) {
                        handleNetworkError(ev.getTarget());
                    }
                }
            } catch (InterruptedException ex) {
                // we've been interrupted, let's just stop then
            }
        }
    }

    private void startDispatcherThread() {
        dispatcherThread = new Thread(new Dispatcher());
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
    }
}
