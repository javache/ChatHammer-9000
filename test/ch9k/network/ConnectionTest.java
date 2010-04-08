package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import org.junit.Test;
import junit.framework.TestCase;
import java.net.InetAddress;

public class ConnectionTest extends TestCase {

    
    private class TestListener implements EventListener {
        private int received;
        
        public TestListener() {
            received = 0;
        }
        
        public void handleEvent(Event ev){
            received++;
        }
        
        public int getReceived(){
            return received;
        }
        
    }
    
    
    @Test
    public void testSendEvent() {
        EventPool pool = EventPool.getInstance();
        TestListener list = new TestListener();
        pool.addListener(list,new TypeEventFilter(TestNetworkEvent.class));
        DirectResponseServer server = new DirectResponseServer();
        server.start();
        try {
            Connection conn = new Connection(InetAddress.getLocalHost());
            conn.sendEvent(new TestNetworkEvent());
            Thread.sleep(10);
            assertEquals(1,list.getReceived());
            conn.sendEvent(new TestNetworkEvent());
            Connection conn2 = new Connection(InetAddress.getLocalHost());
            conn.sendEvent(new TestNetworkEvent());
            conn2.sendEvent(new TestNetworkEvent());
            Thread.sleep(40);
            assertEquals(4,list.getReceived());
        } catch (Exception e) {
            System.out.println(e);
        }
        server.stop();
    }
    
}