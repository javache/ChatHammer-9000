package ch9k.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;

import ch9k.network.events.UserDisconnectedEvent;
import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;

/**
 * 
 * @author nudded
 */
public class Connection {

    /**
     * the default port used to create connections
     * chosen because it's the smallest prime number
     * OVER 9000
     */
    public static int DEFAULT_PORT = 9001;
    
    /**
     * the remote InetAddress
     */
    private InetAddress target;

    /**
     * the socket used to write to the other side
     */
    private Socket socket;
    
    /**
     * Streams used to transfer Objects
     */
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * boolean value to keep the listenThread running
     */
    private boolean keepListening;

    /**
     * Constructor
     * @param ip 
     */
    public Connection(InetAddress ip) throws IOException {
        socket = new Socket(ip,DEFAULT_PORT);
        setup();
    }

    /**
     * constructor used by ConnectionManager, constructs a Connection
     * out of a connected socket.
     * @param s The socket that connected
     */
    public Connection(Socket s) throws IOException {
        socket = s;
        setup();
    }
    
    private void setup() throws IOException {
        keepListening = true;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());        
        startListenThread();
    }
    
    /**
     * close the socket
     */
    public void close() {
        keepListening = false;
        try {
            socket.close();
        } catch(IOException e) {
            
        }
    }
    
    /**
     * sends a PingEvent to the target,
     * if it errors, returns false
     * else true
     */
    public boolean hasConnection() {
        boolean connection = true;
        try {
            sendEvent(new ch9k.network.events.PingEvent());
        } catch (IOException e) {
            connection = false;
        }
        return connection;
    }
    
    /**
     * send a NetworkEvent
     * @param ev The event to be send
     */
    public void sendEvent(NetworkEvent ev) throws IOException {
        sendObject(ev);
    }
    
    /**
     * send an object
     * @param obj the Object to be send
     */
    public void sendObject(Object obj) throws IOException {
        out.writeObject(obj);
    }
    
     /**
      * start listening for incoming events and send them to the EventPool
      */
    private void startListenThread() {
        new Thread(new Runnable(){
            public void run() {
                try {
                    while(keepListening) {
                        Event ev = (Event)in.readObject();
                        EventPool.getInstance().raiseEvent(ev);
                    }
                } catch (EOFException e) {
                    // This happens when the socket on the other side closes
                    EventPool.getInstance().raiseEvent(new UserDisconnectedEvent(target));
                } catch (IOException e) {
                    System.out.println(e);
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                } 
            }
        }).start();
    }
    
}