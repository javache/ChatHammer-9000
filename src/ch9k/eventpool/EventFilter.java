package ch9k.eventpool;

/**
 * Represents a collection of Event-types a listeners wants to receive
 * @author Pieter De Baets
 */
public interface EventFilter {
    /**
     * Check if the listener associated with this eventfilter wants
     * to receive this event
     * @param event
     * @return accepts
     */
    public boolean accept(Event event);
    
    //public String[] getMatchedEventIds();
}
