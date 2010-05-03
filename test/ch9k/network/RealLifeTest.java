package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Test;
import static org.junit.Assert.*;

public class RealLifeTest {    
    private class TestListener implements EventListener {
        public boolean received = false;
        
        public void handleEvent(Event ev) {
            TestNetworkEvent event = (TestNetworkEvent)ev;
            if(event.isExternal()) {
                received = true;
                synchronized(RealLifeTest.this) {
                    RealLifeTest.this.notifyAll();
                }
            }
        }
    }
    
    @Test
    public void letTheBeastGo() throws UnknownHostException, InterruptedException {
        EventPool pool = EventPool.getAppPool();
        TestListener listener = new TestListener();
        pool.addListener(listener,new EventFilter(TestNetworkEvent.class));
        pool.raiseNetworkEvent(new TestNetworkEvent(InetAddress.getByName("zeus.ugent.be")));
        
        synchronized(this) {
            wait(2000);
        }
        assertTrue(listener.received);
    }
}
