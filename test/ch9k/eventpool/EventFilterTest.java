package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventFilterTest {
    public EventFilterTest() {}
    
    /**
     * Test of getEventIds method, of class EventFilter.
     */
    @Test
    public void testGetEventIds() {
        EventFilter instance = new EventFilter();
        assertArrayEquals(null, instance.getEventIds());
    }
}
