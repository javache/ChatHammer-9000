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
     * The socket used to write to the other side
     */
    private Socket socket;

    /**
     * A concurrent queue of events to be sent
     */
    private LinkedBlockingQueue<NetworkEvent> eventQueue =
            new LinkedBlockingQueue<NetworkEvent>();

    /**
     * Streams used to transfer Objects
     */
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Threads that listen and write to the socket
     */
    private Thread listenerThread;
    private Thread writerThread;

    /**
     * The EventPool to send events to
     */
    private EventPool pool;

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
        this.socket = new Socket();
        this.pool = pool;

        new Thread(new Runnable() {
            public void run() {
                try {
                    logger.info("Opening connection to " + ip);
                    socket.connect(new InetSocketAddress(ip, DEFAULT_PORT),
                            SOCKET_CONNECT_TIMEOUT);

                    init();
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
        this.socket = socket;
        this.pool = pool;

        init();
    }

    /**
     * Finish the initialization of the Connection
     * @throws IOException
     */
    private void init() throws IOException {
        socket.setKeepAlive(true);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        String threadName = "Connection-" + socket.getInetAddress().getHostAddress();

        listenerThread = new Thread(new ConnectionListener(), threadName + "-reader");
        listenerThread.setDaemon(true);
        listenerThread.start();

        writerThread = new Thread(new ConnectionWriter(), threadName + "-writer");
        writerThread.setDaemon(true);
        writerThread.start();

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
        
    }
    
    /**
     * Close the socket
     */
    public void close() {
        logger.info("Closing connection to " + socket.getInetAddress());
        try {
            socket.close();
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }

    /**
     * Check if the connection is still alive by sending
     * a PingEvent to the socket
     * @return result Result of the ping-attempt
     */
    public synchronized boolean hasConnection() {
        while (!initialized) {
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        
        try {
            sendObject(new PingEvent(socket.getInetAddress()));
        } catch (IOException ex) {
            logger.info(ex.toString());
            return false;
        }
        
        return true;
    }

    /**
     * Send a NetworkEvent to the remote ip
     * @param event Event to be sent
     */
    public void sendEvent(NetworkEvent event) {
        eventQueue.add(event);
    }

    /**
     * Send an object over the socket
     * @param object Object to be sent
     * @throws IOException
     */
    private synchronized void sendObject(Object object) throws IOException {
        if (out != null) {
            out.writeObject(object);
            out.flush();
        } else {
            throw new SocketException("No connection was made.");
        }
    }

    /**
     * Close the connection, something happened
     */
    private void remoteClosed() {
        pool.raiseEvent(new UserDisconnectedEvent(socket.getInetAddress()));

        // close the output (not the input, since it might still contain messages)
        try {
            socket.shutdownOutput();
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }

    /**
     * Start listening for incoming events and send them to the EventPool
     */
    private class ConnectionListener implements Runnable {
        public void run() {
            try {
                while (!socket.isClosed()) {
                    readEvent();
                }
            } catch (EOFException ex) {
                logger.info(ex.toString());
            } catch (IOException ex) {
                logger.warn(ex.toString());
            }
            
            // this happens when the socket on the remote end closes
            remoteClosed();
        }

        private void readEvent() throws IOException {
            try {
                NetworkEvent ev = (NetworkEvent) in.readObject();
                ev.setSource(socket.getInetAddress());
                logger.info(String.format("Received event %s from %s",
                        ev.getClass().getName(), ev.getSource()));

                // downcast so we don't send it again
                pool.raiseEvent((Event) ev);
            } catch (ClassNotFoundException ex) {
                logger.error(ex.toString());
            }
        }
    }

    /**
     * Send events from the queue
     */
    private class ConnectionWriter implements Runnable {
        public void run() {
            try {
                while (!socket.isClosed()) {
                    try {
                        NetworkEvent event = eventQueue.take();
                        writeEvent(event);
                    } catch (IOException ex) {
                        logger.warn(ex.toString());
                    }
                }
            } catch (InterruptedException ex) {
                // we should just stop then
                logger.info(ex.toString());
            }
        }

        private void writeEvent(NetworkEvent event) throws IOException {
            logger.info(String.format("Sending event %s to %s",
                    event.getClass().getName(), event.getTarget()));
            sendObject(event);
        }
    }
}
