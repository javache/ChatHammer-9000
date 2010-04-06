package ch9k.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import ch9k.eventpool.NetworkEvent;

/**
 * 
 * @author nudded
 */
public class Connection {
    
    private static int DEFAULT_PORT = 1337;
    
    // the remote ip
    private InetAddress target;

    // a queue of events to be send
    private Queue<NetworkEvent> sendQueue;

    // the actual socket used to transfer objects
    private Socket socket;

    // used by the listenThread to see if it should keep listening 
    private boolean keepListening;

    /**
     * Constructor
     * @param ip 
     */
    public Connection(InetAddress ip) throws IOException{
        socket = new Socket(ip,DEFAULT_PORT);
        keepListening = true;
        startListenThread();
    }
    
    public Connection(String host) throws IOException {
        socket = new Socket(host,DEFAULT_PORT);
        keepListening = true;
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
    public void sendEvent(NetworkEvent ev){
        sendObject(ev);
    }
    
    /**
     * send an object
     * @param obj the Object to be send
     */
    public void sendObject(Object obj) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(obj);
        } catch(IOException e) {
            // TODO do something with that exception
        }
    }
    
    /**
     * start listening for incoming events and send them to the EventPool
     */
    private void startListenThread() {
        new Thread(new Runnable(){
            public void run() {
                try {
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    while(keepListening) {
                        NetworkEvent ev = (NetworkEvent)in.readObject();
                        EventPool.getInstance().raiseEvent(ev);
                    }
                } catch (IOException e) {
                    
                } catch (ClassNotFoundException e) {
                    
                }
            }
        }).start();
    }
    
}