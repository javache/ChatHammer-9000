package ch9k.eventpool;

/**
 * Represents a collection of Event-types a listeners wants to receive
 * @author Pieter De Baets
 */
public interface EventFilter {
    public String[] getMatchedEventIds();
}
