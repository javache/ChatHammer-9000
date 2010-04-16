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
    
    /**
     * reader and writer for the socket
     */
    private EventWriter writer;
    private EventReader reader;
    
    /**
     * gotta keep a hold of this socket
     */
    private Socket socket;
    
    /**
     * the partypool!
     */
    private EventPool pool;
    
    /**
     * we need to know which connection we belong to,
     * to report errors
     */
    private Connection connection;
    
    /**
     * i'm getting tired of these loggers
     */
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
    
    /**
     * the user disconnected
     */
    public void receivedEOF() {
        /* TODO throw appropriate event */
        connection.socketHandlerClosed(this);
        try {
            close();
        } catch (IOException e) {
            
        }
    }   
    
    /**
     * an error during writing? better close ourselves
     */
    public void writingFailed() {
        connection.socketHandlerClosed(this);
        try {
            close();
        } catch (IOException e) {
            
        }
    } 
    
    /**
     * this will close the socket
     * and kill the reader and writer
     */
    public void close() throws IOException {
        logger.info("Closing connection to " + socket.getInetAddress());
        socket.shutdownOutput();
        socket.close();
    }
    
    /**
     * do some processing on the event before it is send to the EventPool
     */
    public void process(NetworkEvent event) {
        event.setSource(socket.getInetAddress());
    }
}