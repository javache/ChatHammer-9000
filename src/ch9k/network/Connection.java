package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.events.PingEvent;
import ch9k.network.events.UserDisconnectedEvent;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
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
    private static final int SOCKET_CONNECT_TIMEOUT = 500;

    /**
     * I'm a lumberjack, and I'm okay.
     * I sleep all night and I work all day.
     *
     * Oh wait, that's a different kind of logging.
     */
    private static final Logger logger = Logger.getLogger(Connection.class);

    /**
     * A concurrent queue of events to be sent
     */
    private LinkedBlockingQueue<NetworkEvent> eventQueue =
            new LinkedBlockingQueue<NetworkEvent>();

    /**
     * keep track of all the sockethandlers we have
     */
    private ArrayList<SocketHandler> handlerList = new ArrayList<SocketHandler>();
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
     * Setup a new connection, will asynchronously create a connection to the
     * given InetAddress
     * @param ip
     * @param pool
     * @param manager 
     */
    public Connection(final InetAddress ip, EventPool pool, final ConnectionManager manager) {
        final Socket socket = new Socket();
        this.pool = pool;
        this.target = ip;
        
        new Thread(new Runnable() {
            public void run() {
                try {
                    logger.info("Opening connection to " + ip);
                    socket.connect(new InetSocketAddress(ip, DEFAULT_PORT),
                            SOCKET_CONNECT_TIMEOUT);

                    init(socket);
                } catch (IOException ex) {
                    logger.warn(ex.toString());

                    if (manager != null) {
                        manager.handleNetworkError(ip);
                    }

                    notifyInitComplete();
                }
            }
        }).start();
    }

    /**
     * Construct a Connection out of an already connected socket.
     * @param socket The socket that connected
     * @param pool The EventPool this connection will use
     * @throws IOException
     */
    public Connection(Socket socket, EventPool pool) throws IOException {
        this.pool = pool;
        this.target = socket.getInetAddress();
        
        init(socket);
    }

    /**
     * Finish the initialization of the Connection
     * @throws IOException
     */
    private void init(Socket socket) throws IOException {
        socket.setKeepAlive(true);

        handlerList.add(new SocketHandler(socket,eventQueue,pool,this));
        
        notifyInitComplete();
    }

    /**
     * Mark connection as not connecting anymore
     */
    private synchronized void notifyInitComplete() {
        initialized = true;
        notifyAll();
    }

    public void socketHandlerClosed(SocketHandler handler) {
        /* TODO we need to relay this back to the manager */
        handlerList.remove(handler);
        logger.warn("Connection closed");
        /* no connections left -> userdisconnected */
        if (handlerList.size() == 0) {
            pool.raiseEvent(new UserDisconnectedEvent(target));
        }
    }
    
    /**
     * Close the socket
     */
    public void close() {
        try {
            for (SocketHandler handler : handlerList) {
                handler.close();                
            }
            handlerList.clear();
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }

    /**
     * TODO make this a proper hasConnection
     * will return true for the time being
     */
    public synchronized boolean hasConnection() {
        return true;
    }

    /**
     * Send a NetworkEvent to the remote ip
     * @param event Event to be sent
     */
    public void sendEvent(NetworkEvent event) {
        eventQueue.add(event);
    }

}
