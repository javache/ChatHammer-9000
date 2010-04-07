package ch9k.network;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.ConnectException;


import java.util.Queue;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;

import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;

/**
 * 
 * @author nudded
 */
public class Connection {
    
    public static int DEFAULT_PORT = 9001;
    
    // the remote ip
    private InetAddress target;

    // a queue of events to be send
    private Queue<NetworkEvent> sendQueue;

    // the actual socket used to transfer objects
    private Socket socket;
    
    // the 2 streams used to transfer objects
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // used by the listenThread to see if it should keep listening 
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
                        NetworkEvent ev = (NetworkEvent)in.readObject();
                        // down cast because it will end up in infinite loop otherwise (WTF)
                        EventPool.getInstance().raiseEvent((Event)ev);
                    }
                } catch (EOFException e) {
                    // This happens when the socket on the other side closes
                    // TODO handle that case properly
                } catch (IOException e) {
                    System.out.println(e);
                } catch (ClassNotFoundException e) {
                    System.out.println(e);
                } 
            }
        }).start();
    }
    
}