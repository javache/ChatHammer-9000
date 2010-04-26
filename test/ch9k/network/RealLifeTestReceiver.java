package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;

public class RealLifeTestReceiver {
    private static class EchoListener implements EventListener {
        public void handleEvent(Event ev) {
            TestNetworkEvent event = (TestNetworkEvent) ev;
            if (event.isExternal()) {
                EventPool.getAppPool().raiseEvent(new TestNetworkEvent(event.getSource()));
            }
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        EventPool pool = EventPool.getAppPool();
        pool.addListener(new EchoListener(), new EventFilter(TestNetworkEvent.class));
        while(true) Thread.sleep(1000);	
    }
}
