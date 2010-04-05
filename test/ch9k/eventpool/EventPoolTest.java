package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventPoolTest {
    /**
     * Test of addListener method, of class EventPool.
     */
    @Test
    public void testAddListener() {
        EventListener listener = null;
        EventFilter filter = null;
        EventPool instance = new EventPool();
        instance.addListener(listener, filter);
        // TODO review the generated test code and remove the default call to fail.
        // fail("The test case is a prototype.");
    }

    /**
     * Test of raiseEvent method, of class EventPool.
     */
    @Test
    public void testRaiseEvent_Event() {
        Event event = null;
        EventPool instance = new EventPool();
        instance.raiseEvent(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of raiseEvent method, of class EventPool.
     */
    @Test
    public void testRaiseEvent_NetworkEvent() {
        NetworkEvent networkEvent = null;
        EventPool instance = new EventPool();
        instance.raiseEvent(networkEvent);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}