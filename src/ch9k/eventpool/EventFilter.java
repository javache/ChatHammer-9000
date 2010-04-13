package ch9k.eventpool;

/**
 * Represents a collection of Event-types a listeners wants to receive
 * @author Pieter De Baets
 */
public class EventFilter {
    private Class<? extends Event> type;

    /**
     * Construct a EventFilter that filters by type
     * @param type
     */
    public EventFilter(Class<? extends Event> type) {
        this.type = type;
    }
    
    /**
     * Check if the listener associated with this eventfilter wants
     * to receive this event
     * @param event
     * @return accepts
     */
    public boolean accept(Event event) {
        return type.isInstance(event);
    }
    
    //public String[] getMatchedEventIds();
}
