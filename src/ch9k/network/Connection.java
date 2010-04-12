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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public static final int DEFAULT_PORT = 9001;

    /**
     * Amount of time to wait for a socket on connect
     */
    private static final int SOCKET_CONNECT_TIMEOUT = 500;
    
    /**
     * The socket used to write to the other side
     */
    private Socket socket;

    /**
     * I'm a lumberjack, and I'm okay.
     * I sleep all night and I work all day.
     *
     * Oh wait, that's a different kind of logging.
     */
    private static final Logger LOGGER =
            Logger.getLogger(Connection.class.getName());

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
     * Thread that listens to the socket
     */
    private Thread listenerThread;

    /**
     * Thread that writes to the socket
     */
    private Thread writerThread;
    
    /**
     * The EventPool to send events to
     */
     private EventPool pool;

     /**
      * True if the connection is still connecting, do not disturb
      */
     private boolean connecting = true;

    /**
     * Constructor
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
                    LOGGER.info("Opening connection to " + ip);
                    socket.connect(new InetSocketAddress(ip, DEFAULT_PORT),
                            SOCKET_CONNECT_TIMEOUT);
                    init();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                    
                    if(manager != null) {
                        manager.handleNetworkError(ip);
                    }

                    notifyInitComplete();
                }
            }
        }).start();
    }

    /**
     * constructor used by ConnectionManager, constructs a Connection
     * out of a connected socket.
     * @param socket The socket that connected
     * @param pool The EventPool this connection will use
     * @throws IOException
     */
    public Connection(Socket socket, EventPool pool) throws IOException {
        this.socket = socket;
        this.pool = pool;
        
        init();
    }

    private void init() throws IOException {
        socket.setKeepAlive(true);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());        

        String threadName = "connection-" + socket.getInetAddress().getHostAddress();

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
        connecting = false;
        notifyAll();
    }
    
    /**
     * close the socket
     */
    public void close() {
        LOGGER.info("Closing connection to " + socket.getInetAddress());
        try {
            socket.close();
        } catch(IOException e) {
            // TODO handle error?
            System.out.println(e);
        }
    }
    
    /**
     * sends a PingEvent to the target
     * @return false if pinging the target gives an exception
     */
    public synchronized boolean hasConnection() {
        while(connecting) {
            try {
                wait();
            } catch (InterruptedException ex) {}
        }
        
        try {
            sendObject(new PingEvent(socket.getInetAddress()));
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    /**
     * send a NetworkEvent
     * @param ev The event to be send
     */
    public void sendEvent(NetworkEvent ev) {
        eventQueue.add(ev);
    }
    
    /**
     * send an object
     * @param obj the Object to be send
     * @throws IOException
     */
    private synchronized void sendObject(Object obj) throws IOException {
        if(out != null) {
            out.writeObject(obj);
            out.flush();
        } else {
            throw new SocketException("No connection was made.");
        }
    }
    
    private void remoteClosed() {
        pool.raiseEvent(new UserDisconnectedEvent(socket.getInetAddress()));

        // close the output (not the input, since it might still contain messages)
        try {
            socket.shutdownOutput();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Start listening for incoming events and send them to the EventPool
     */
    private class ConnectionListener implements Runnable {
        public void run() {
            try {
                while(!socket.isClosed()) {
                    readEvent();
                }
            } catch (EOFException ex) {
                LOGGER.log(Level.INFO, ex.toString());
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            // this happens when the socket on the remote end closes
            remoteClosed();
        }

        private void readEvent() throws IOException {
            try {
                NetworkEvent ev = (NetworkEvent)in.readObject();
                ev.setSource(socket.getInetAddress());
                LOGGER.info(String.format("Received event %s from %s",
                        ev.getClass().getName(), ev.getSource()));

                // downcast so we don't send it again
                pool.raiseEvent((Event)ev);
            } catch (ClassNotFoundException ex) {
                // TODO handle error
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Send events from the queue
     */
    private class ConnectionWriter implements Runnable {
        public void run() {
            try {
                while(!socket.isClosed()) {
                    try {
                        NetworkEvent ev = eventQueue.take();
                        LOGGER.info(String.format("Sending event %s to %s",
                                ev.getClass().getName(), ev.getTarget()));
                        sendObject(ev);
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            } catch(InterruptedException ex) {
                // we should just stop then
                LOGGER.log(Level.INFO, ex.toString());
            }
        }
    }
}
