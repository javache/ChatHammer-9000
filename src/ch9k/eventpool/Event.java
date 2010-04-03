package ch9k.eventpool;

/**
 * Base event class
 * @author Pieter De Baets
 */
public abstract class Event {
    private boolean handled;

    /**
     * Check if another eventlistener has already marked this event
     * as 'handled'
     * @return handled
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * Mark this event as 'handled'
     * @param handled
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
