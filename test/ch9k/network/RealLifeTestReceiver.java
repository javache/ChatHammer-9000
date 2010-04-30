package ch9k.network;

import ch9k.chat.Contact;
import ch9k.chat.event.ContactOnlineEvent;
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

        // just return all network events
        pool.addListener(new EchoListener(), new EventFilter(TestNetworkEvent.class));

        // answer all online events with "yes, i'm online too"
        pool.addListener(new EventListener() {
            public void handleEvent(Event event) {
                ContactOnlineEvent onlineEvent = (ContactOnlineEvent) event;
                Contact remoteContact = new Contact(onlineEvent.getSender(),
                        onlineEvent.getSource());
                EventPool.getAppPool().raiseEvent(new ContactOnlineEvent(remoteContact));
            }
        }, new EventFilter(ContactOnlineEvent.class));

        while(true) {
            Thread.sleep(1000);
        }
    }
}
