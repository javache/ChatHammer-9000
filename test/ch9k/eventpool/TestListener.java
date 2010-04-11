package ch9k.eventpool;

/**
 * Listener that will just store the event it last received
 * @author Pieter De Baets
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
}
