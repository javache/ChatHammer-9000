package ch9k.eventpool;

/**
 * Interface for Event-listeners
 * @author Pieter De Baets
 */
public interface EventListener {
    /**
     * Handle the occurence of a certain event
     * @param event
     */
    public void handleEvent(Event event);
}
