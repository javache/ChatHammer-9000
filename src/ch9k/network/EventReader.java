package ch9k.network;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;
import org.apache.log4j.Logger;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.Event;

public class EventReader implements Runnable {
    
    /**
     * Events will be read from this stream
     */
    private final ObjectInputStream in;
    
    /**
     * no poolparty without a pool!
     */
    private final EventPool pool;
    
    /**
     * he's here to help, in case something bad happens
     */
    private final ErrorHandler errorHandler;
    
    /**
     * processing is all he does, all day long
     */
    private final EventProcessor processor;
    
    /**
     * the usual logger.
     */
    private static final Logger logger = Logger.getLogger(EventReader.class);
    
    public EventReader(ObjectInputStream stream,EventPool pool,EventProcessor processor,ErrorHandler handler) {
        this.in = stream;
        this.pool = pool;
        this.processor = processor;
        this.errorHandler = handler;
    }
    
    /**
     * Run forever, reading events from the inputstream
     */
    public void run() {
        try {
            while(true) {
                try {
                    NetworkEvent event = (NetworkEvent)in.readObject();
                    processor.process(event);
                    logger.info(String.format("Received event %s from %s",
                            event.getClass().getName(), event.getSource()));
                    pool.raiseEvent((Event)event);
                } catch (ClassNotFoundException e) {
                    logger.warn(e.toString());
                }
            } 
            
        } catch(EOFException e) { 
            errorHandler.receivedEOF();
            logger.warn(e.toString());
        } catch (IOException e) {
            logger.warn(e.toString());
        }
    }
    
}