package ch9k.eventpool;

import java.util.Date;

/**
 * Base event class
 * @author Pieter De Baets
 */
public abstract class Event {
    private boolean handled;
    private Date createdAt;

    public Event() {
        handled = false;
        createdAt = new Date();
    }

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

    /**
     * Get the time at which this event was created
     * @return date
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Get the object that originally sent this event
     * @return source
     */
    public abstract Object getSource();
}
