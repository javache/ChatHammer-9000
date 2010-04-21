package ch9k.eventpool;

/**
 * Listener that will just store the event it last received
 */
public class TestListener implements EventListener {
    public Event lastReceivedEvent = null;
    public int receiveCount = 0;
    
    @Override
    public void handleEvent(Event event) {
        lastReceivedEvent = event;
        receiveCount++;
    }

    public int getCount() {
        return receiveCount;
    }

    public Event getLastEvent() {
        return lastReceivedEvent;
    }
}
