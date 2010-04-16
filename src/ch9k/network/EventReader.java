package ch9k.network;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;
import org.apache.log4j.Logger;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.eventpool.Event;

public class EventReader implements Runnable {
    
    private final ObjectInputStream in;

    private final EventPool pool;
    
    private final ErrorHandler errorHandler;
    
    private final EventProcessor processor;
    
    private static final Logger logger = Logger.getLogger(EventReader.class);
    
    public EventReader(ObjectInputStream stream,EventPool pool,EventProcessor processor,ErrorHandler handler) {
        this.in = stream;
        this.pool = pool;
        this.processor = processor;
        this.errorHandler = handler;
    }
    
    public void run() {
        try {
            while(true) {
                try {
                    NetworkEvent event = (NetworkEvent)in.readObject();
                    logger.info(String.format("Received event %s from %s",
                            event.getClass().getName(), event.getSource()));
                    processor.process(event);
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