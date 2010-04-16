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
import org.apache.log4j.Logger;

import ch9k.network.events.UserDisconnectedEvent;


public class SocketHandler implements ErrorHandler, EventProcessor {
    
    private EventWriter writer;
    private EventReader reader;
    
    private Socket socket;
    private EventPool pool;
    
    private Connection connection;
    
    private static final Logger logger = Logger.getLogger(SocketHandler.class);
    
    public SocketHandler(Socket socket, BlockingQueue<NetworkEvent> queue, EventPool pool,Connection conn) throws IOException {
        this.socket = socket;
        this.pool = pool;
        this.connection = conn;
        
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
        connection.socketHandlerClosed(this);
        try {
            close();
        } catch (IOException e) {
            
        }
    }   
    
    public void writingFailed() {
        try {
            close();
        } catch (IOException e) {
            
        }
    } 
    
    public void close() throws IOException {
        logger.info("Closing connection to " + socket.getInetAddress());
        socket.shutdownOutput();
        socket.close();
    }
    
    public void process(NetworkEvent event) {
        event.setSource(socket.getInetAddress());
    }
}