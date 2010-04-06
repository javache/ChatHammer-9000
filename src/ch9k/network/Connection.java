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

    /**
     * Constructor
     * @param ip 
     */
    public Connection(InetAddress ip) {
        target = ip;
    }
    
    /**
     * Send a NetworkEvent
     * @param networkEvent
     */
    public void sendEvent(NetworkEvent networkEvent) {
        Socket socket;
        try {
            socket = new Socket(target,DEFAULT_PORT);
            ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
            stream.writeObject(networkEvent);
        } catch(IOException e) {
            // TODO do something with that exception
        }
    }
    
}