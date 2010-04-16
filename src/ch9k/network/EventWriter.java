package ch9k.network;

import java.io.ObjectOutputStream;
import java.io.Closeable;
import java.io.IOException;
import ch9k.eventpool.NetworkEvent;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

public class EventWriter implements Runnable,Closeable{
    
    /**
     * The ObjectOutputStream to write to;
     */
    private ObjectOutputStream out;
    
    /**
     * A logger, to log our efforts
     */
    private static final Logger logger = Logger.getLogger(EventWriter.class);
    
    /**
     * We all need a little help from time to time
     */
    private final ErrorHandler errorHandler;
    
    /**
     * Queue to take events from
     */
    private final BlockingQueue<NetworkEvent> queue;
    
    /**
     * Since i don't want to test for socket.isClosed() 
     * If true, the writer will stay alive
     */
    private boolean shouldContinue = true;
    
    public EventWriter(ObjectOutputStream stream,ErrorHandler handler,BlockingQueue<NetworkEvent> queue) {
        this.out = stream;
        this.errorHandler = handler;
        this.queue = queue;
    }
    
    /**
     * close the ObjectOutputStream
     */
    public void close() throws IOException {
        shouldContinue = false;
        out.close();
    }
    
    /**
     * send an event directly (bypassing the queue)
     * useful for testing if the outputstream is still alive
     */
    public synchronized void sendEvent(NetworkEvent event) throws IOException {
        logger.info(String.format("Sending event %s to %s",
                event.getClass().getName(), event.getTarget()));
        out.writeObject(event);
        out.flush();
    }
    
    /**
     * run until close is called, or we encounter an error
     * take events from the queue and send'em
     */
    public void run() {
        try {
            while(shouldContinue) {
                try {
                    NetworkEvent event = queue.poll(200,TimeUnit.MILLISECONDS);
                    if(event == null) continue;

                    sendEvent(event);
                } catch(InterruptedException e) {
                    logger.warn(e.toString());
                }
            }
            
        } catch(IOException e) {
            logger.info(e.toString());
            errorHandler.writingFailed();
        }
    }
    
}