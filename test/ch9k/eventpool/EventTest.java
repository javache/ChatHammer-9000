package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventTest {
    /**
     * Test of isHandledsetHandled method, of class Event.
     */
    @Test
    public void testHandled() {
        Event instance = new EventImpl();
        assertEquals(false, instance.isHandled());
        instance.setHandled(true);
        assertEquals(true, instance.isHandled());
    }

    public class EventImpl extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}
