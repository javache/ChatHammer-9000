package ch9k.network;

import ch9k.eventpool.DataEvent;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.event.UserDisconnectedEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 * Connection sends and receives event over a socket
 * @author nudded
 * @author Pieter De Baets
 */
public class Connection {
    /**
     * The default port used to create connections
     * chosen because it's the smallest prime number
     * OVER 9000
     */
    public static final int DEFAULT_PORT = 4011;

    /**
     * Amount of time to wait for a socket on connect
     */
    private static final int SOCKET_CONNECT_TIMEOUT = 1000;

    /**
     * I'm a lumberjack, and I'm okay.
     * I sleep all night and I work all day.
     *
     * Oh wait, that's a different kind of logging.
     */
    private static final Logger logger = Logger.getLogger(Connection.class);

    /**
     * A concurrent queue of NetworkEvents to be sent
     */
    private LinkedBlockingQueue<NetworkEvent> eventQueue =
            new LinkedBlockingQueue<NetworkEvent>();

    /**
     * SocketHandler for the basic NetworkEvents;
     */
    private SocketHandler eventSocketHandler;
    
    /**
     * A concurrent queue of DataEvents to be sent
     */
    private LinkedBlockingQueue<NetworkEvent> dataQueue =
            new LinkedBlockingQueue<NetworkEvent>();
    
    /**
     * SocketHandler for DataEvents
     */
    private SocketHandler dataSocketHandler;
    
    /**
     * The EventPool to send events to
     */
    private EventPool pool;
    
    /**
     * which ip are we talking to?
     */
    private InetAddress target;

    /**
     * True if the connection is still connecting, do not disturb
     */
    private boolean initialized = false;

    /**
     * Connectionmanager
     */
    private ConnectionManager connectionManager;

    /**
     * Setup a new connection, will asynchronously create a connection to the
     * given InetAddress
     * @param ip
     * @param pool
     * @param manager 
     */
    public Connection(final InetAddress ip, EventPool pool, final ConnectionManager manager) {
        this.target = ip;
        this.pool = pool;
        this.connectionManager = manager;

        final Socket socket = new Socket();
       
        new Thread(new Runnable() {
            public void run() {
                try {
                    logger.info("Opening connection to " + ip);
                    socket.connect(new InetSocketAddress(ip, DEFAULT_PORT),
                            SOCKET_CONNECT_TIMEOUT);

                    init(socket);
                } catch (IOException ex) {
                    logger.warn(ex.toString());

                    // @TODO show notice in statusbar

                    if (manager != null) {
                        manager.handleNetworkError(ip);
                    }
                    
                    notifyInitComplete();
                }
            }
        }, "Connection-" + ip.getHostAddress() + "-starter").start();
    }

    /**
     * Construct a Connection out of an already connected socket.
     * @param socket The socket that connected
     * @param pool The EventPool this connection will use
     * @param connectionManager 
     * @throws IOException
     */
    public Connection(Socket socket, EventPool pool, ConnectionManager connectionManager) throws IOException {
        this.pool = pool;
        this.target = socket.getInetAddress();
        this.connectionManager = connectionManager;
        
        init(socket);
    }

    /**
     * Finish the initialization of the Connection
     * @throws IOException
     */
    private void init(Socket socket) throws IOException {
        socket.setKeepAlive(true);

        socket.setSoTimeout(20000);
        eventSocketHandler = new SocketHandler(socket,eventQueue,pool,this);
        
        notifyInitComplete();
    }
    
    public void addDataSocket(Socket socket) throws IOException {
        socket.setKeepAlive(true);
        dataSocketHandler = new SocketHandler(socket,dataQueue,pool,this);
    }
    
    private void initDataSocket() {
        final Socket socket = new Socket();
        new Thread(new Runnable() {
            public void run() {
                try {
                    logger.info("Opening connection to " + target);
                    socket.connect(new InetSocketAddress(target, DEFAULT_PORT),
                            SOCKET_CONNECT_TIMEOUT);
                    socket.setKeepAlive(true);
                    dataSocketHandler = new SocketHandler(socket, dataQueue, pool, Connection.this);
                } catch (IOException ex) {
                    logger.warn(ex.toString());
                }
            }
        }).start();
    }
    
    /**
     * Mark connection as not connecting anymore
     */
    private synchronized void notifyInitComplete() {
        initialized = true;
        notifyAll();
    }

    public void socketHandlerClosed(SocketHandler handler) {
        // close all connections
        close();
        
        connectionManager.handleNetworkError(target);
        logger.warn("Connection closed to " + target.toString());
        pool.raiseEvent(new UserDisconnectedEvent(target));
    }
    
    /**
     * Close the socket
     */
    public void close() {
        try {
            if(eventSocketHandler != null) {
                eventSocketHandler.close();
            }
            if(dataSocketHandler != null) {
                dataSocketHandler.close();
            }
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }

    /**
     * TODO make this a proper hasConnection
     * will return true for the time being
     * @return connected
     */
    public synchronized boolean hasConnection() {
        throw new RuntimeException("hasConnection not implemented");
    }

    /**
     * Send a NetworkEvent to the remote ip
     * @param event Event to be sent
     */
    public void sendEvent(NetworkEvent event) {
        if(event instanceof DataEvent) {
            if(dataSocketHandler == null) initDataSocket();
            dataQueue.add(event);
        } else {
            eventQueue.add(event);
        }
        
    }

}
