package ch9k.eventpool;

/**
 * Sends a message to be displayed in the main app window
 * @author Pieter De Baets
 */
public class StatusEvent extends Event {
    private Object source;
    private String message;

    /**
     * Construct a new ChatApplicationStatusEvent
     * @param source
     * @param message
     */
    public StatusEvent(Object source, String message) {
        this.source = source;
        this.message = message;
    }

    /**
     * Get the value of source
     * @return the value of source
     */
    @Override
    public Object getSource() {
        return source;
    }


    /**
     * Get the value of message
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }
}
