package ch9k.eventpool;

/**
 * An EventFilter that is only interested in the type of the events
 * @author Pieter De Baets
 */
public class TypeEventFilter implements EventFilter {
    private String type;

    /**
     * Construct a new TypeEventFilter
     * @param type
     */
    public TypeEventFilter(Class<? extends Event> type) {
        this.type = type.getName();
    }

    @Override
    public String[] getMatchedEventIds() {
        return new String[] { type };
    }
}
