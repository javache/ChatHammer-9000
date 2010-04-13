package ch9k.network;


import ch9k.eventpool.*;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class RealLifeTestReceiver {
    
    private static class blahListener implements EventListener {
        public void handleEvent(Event ev) {
            TestNetworkEvent event = (TestNetworkEvent)ev;
            if (event.isExternal()) {
                EventPool.getAppPool().raiseEvent(new TestNetworkEvent(event.getSource()));
            }
        }
    }
    
    public static void main(String[] args) {
        EventPool pool = EventPool.getAppPool();
        pool.addListener(new blahListener(),new TypeEventFilter(TestNetworkEvent.class));
        while(true) {
            
        }
    }
    
}