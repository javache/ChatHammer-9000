
package ch9k.eventpool;

/**
 *
 * @author Pieter De Baets
 */
public class WarningEvent extends StatusEvent {
    /**
     * Constructor.
     * @param source Object where the warning originated.
     * @param warningMessage Warning message to raise.
     */
    public WarningEvent(Object source, String warningMessage) {
        super(source, warningMessage, false);
    }

    /**
     * Create a new WarningMessageEvent and raise it to the application-wide
     * event pool.
     * @param source Object where the warning originated.
     * @param warningMessage Warning message to raise.
     */
    public static void raise(Object source,
            String warningMessage) {
        EventPool pool = EventPool.getAppPool();
        Event event = new WarningEvent(source, warningMessage);
        pool.raiseEvent(event);
    }
}
