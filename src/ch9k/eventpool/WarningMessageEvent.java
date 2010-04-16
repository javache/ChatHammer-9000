package ch9k.eventpool;

/**
 * Event type used for asynchronous warnings. These events can be raised from
 * anywhere, as a non-blocking call. The warning will then be shown in the
 * status bar or elsewhere in the GUI.
 * @author Jasper Van der Jeugt
 */
public class WarningMessageEvent extends Event {
    /**
     * Source of the event.
     */
    private Object source;

    /**
     * The warning message.
     */
    private String warningMessage;

    /**
     * Constructor.
     * @param source Object where the warning originated.
     * @param warningMessage Warning message to raise.
     */
    public WarningMessageEvent(Object source, String warningMessage) {
        this.source = source;
        // TODO: Localize at this point.
        this.warningMessage = warningMessage;
    }

    /**
     * Get the warning message. This returns a human-readable message
     * informative to the user.
     * @return The warning message.
     */
    public String getWarningMessage() {
        return warningMessage;
    }

    @Override
    public Object getSource() {
        return source;
    }

    /**
     * Create a new WarningMessageEvent and raise it to the application-wide
     * event pool.
     * @param source Object where the warning originated.
     * @param warningMessage Warning message to raise.
     */
    public static void raiseWarningMessageEvent(Object source,
            String warningMessage) {
        EventPool pool = EventPool.getAppPool();
        Event event = new WarningMessageEvent(source, warningMessage);
        pool.raiseEvent(event);
    }
}
