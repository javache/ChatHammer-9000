package ch9k.network;

import java.util.concurrent.BlockingQueue;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.network.events.PingEvent;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;


public class SocketHandler implements ErrorHandler, EventProcessor {
    
    private EventWriter writer;
    private EventReader reader;
    
    public SocketHandler(Socket socket, BlockingQueue<NetworkEvent> queue, EventPool pool) throws IOException {
        
        String threadName = "Connection-" + socket.getInetAddress().getHostAddress();
        
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(new PingEvent(socket.getInetAddress()));
        writer = new EventWriter(out,this,queue);
        
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        reader = new EventReader(in,pool,this,this);
        
        new Thread(reader,threadName + "-reader").start();
        new Thread(writer,threadName + "-writer").start();
    }
    
    public void receivedEOF() {
        
    }   
    
    public void writingFailed() {
        
    } 
    
    public void process(NetworkEvent event) {
        
    }
}