package ch9k.eventpool;

/**
 * Abstract event class that allows setting of the source object at
 * construction time.
 * @author Jasper Van der Jeugt
 */
public class AbstractEvent extends Event {
    /**
     * The event source.
     */
    private Object source;

    /**
     * Constructor.
     * @param source Source of the event.
     */
    public AbstractEvent(Object source) {
        this.source = source;
    }

    @Override
    public Object getSource() {
        return source;
    }
}
