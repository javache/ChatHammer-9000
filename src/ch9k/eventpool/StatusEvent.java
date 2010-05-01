package ch9k.eventpool;

/**
 * Sends a message to be displayed in the main app window
 * @author Pieter De Baets
 */
public class StatusEvent extends Event {
    private Object source;
    private String message;
    private boolean autoFade;

    /**
     * Construct a new ChatApplicationStatusEvent
     * @param source
     * @param message
     * @param autoFade
     */
    public StatusEvent(Object source, String message, boolean autoFade) {
        this.source = source;
        this.message = message;
        this.autoFade = autoFade;
    }

    /**
     * Construct a new ChatApplicationStatusEvent
     * @param source
     * @param message
     */
    public StatusEvent(Object source, String message) {
        this(source, message, true);
    }

    /**
     * Get the value of autoFade
     * @return the value of autoFade
     */
    public boolean isAutoFade() {
        return autoFade;
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
