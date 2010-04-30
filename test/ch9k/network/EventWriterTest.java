package ch9k.network;

import ch9k.eventpool.NetworkEvent;
import ch9k.network.event.PingEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventWriterTest {
    
    private class Handler implements ErrorHandler {
        public void receivedEOF(){
            
        }
        
        public void writingFailed(){
            fail("sending event failed");
        }
    }
    
    @Test
    public void testSendEvent() throws Exception {
        /* just a temp file, will be deleted */
        File file = File.createTempFile("temp","toon");
        file.createNewFile();
        
        /* hook up our input and outputstreams */
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        /* we could add more logic to this handler, but this will do for now */
        ErrorHandler handler = new Handler();
        /* technically we don't even use this queue */
        BlockingQueue<NetworkEvent> queue = new LinkedBlockingQueue<NetworkEvent>();
        
        /* send an event (synchronous) and read it back */
        EventWriter writer = new EventWriter(stream,handler,queue);
        writer.sendEvent(new PingEvent(InetAddress.getLocalHost()));
        NetworkEvent ev = (NetworkEvent)in.readObject();
        
        /* we send a PingEvent, so this should not fail and be true */
        assertTrue(ev instanceof PingEvent);
        /* delete our file */
        file.delete();
    }
    
    
}
