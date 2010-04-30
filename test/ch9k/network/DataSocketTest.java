package ch9k.network;

import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TestListener;
import java.net.InetAddress;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataSocketTest {
    
    @Test
    public void testSendDataEvent() throws Exception {
        TestListener listener = new TestListener();
        TestListener listener2 = new TestListener();
        EventPool.getAppPool().addListener(listener,new EventFilter(TestDataEvent.class));
        EventPool.getAppPool().addListener(listener2,new EventFilter(TestNetworkEvent.class));
        EventPool.getAppPool().raiseEvent(new TestDataEvent(InetAddress.getLocalHost()));

        Thread.sleep(1000);
        /* we expect 2 because it will be broadcasted locally too */
        assertEquals(2,listener.getCount());

        /* also we should be able to just send regular events */
        EventPool.getAppPool().raiseEvent(new TestNetworkEvent(InetAddress.getLocalHost()));
        EventPool.getAppPool().raiseEvent(new TestDataEvent(InetAddress.getLocalHost()));
        
        Thread.sleep(1000);
        assertEquals(2,listener2.getCount());
        assertEquals(4,listener.getCount());
    }
    
}
