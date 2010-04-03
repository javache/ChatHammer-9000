package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventTest {
    public EventTest() {}

    /**
     * Test of isHandled method, of class Event.
     */
    @Test
    public void testIsHandled() {
        Event instance = new EventImpl();
        assertEquals(false, instance.isHandled());
    }

    /**
     * Test of setHandled method, of class Event.
     */
    @Test
    public void testSetHandled() {
        Event instance = new EventImpl();
        instance.setHandled(true);
        assertEquals(true, instance.isHandled());
    }

    public class EventImpl extends Event {}
}
