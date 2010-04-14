package ch9k.network;

import ch9k.eventpool.EventPool;
import java.net.InetAddress;
import org.junit.Test;
import static org.junit.Assert.*;

public class RealLifeTest {
<<<<<<< HEAD
=======
    
    private class TestListener implements EventListener {
        
        public boolean received = false;
        
        public void handleEvent(Event ev) {
            TestNetworkEvent event = (TestNetworkEvent)ev;
            if(event.isExternal()) {
                received = true;
                synchronized(this) {
                    notifyAll();
                }
            }
        }
        
    }
    
    @Test
    public void letTheBeastGo() throws Exception {
        EventPool pool = EventPool.getAppPool();
        TestListener listener = new TestListener();
        pool.addListener(listener,new EventFilter(TestNetworkEvent.class));
        pool.raiseEvent(new TestNetworkEvent(InetAddress.getByName("zeus.ugent.be")));
        synchronized(this) {
            wait(600);            
        }
        assertTrue(listener.received);
    }
}
