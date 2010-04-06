package ch9k.network;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Queue;
import java.io.ObjectOutputStream;
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

    private Socket socket;

    /**
     * Constructor
     * @param ip 
     */
    public Connection(InetAddress ip) throws IOException{
        socket = new Socket(ip,DEFAULT_PORT);
    }
    
    public Connection(String host) throws IOException {
        socket = new Socket(host,DEFAULT_PORT);        
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
    
}