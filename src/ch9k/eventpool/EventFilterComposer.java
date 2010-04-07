
package ch9k.eventpool;

/**
 * Composes multiple event-filters as one.
 * TODO: maybe simplify this abstraction into one class?
 * @author Pieter De Baets
 */
public class EventFilterComposer implements EventFilter {
    EventFilter a, b;

    public EventFilterComposer(EventFilter a, EventFilter b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public boolean accept(Event event) {
        return a.accept(event) && b.accept(event);
    }
}
