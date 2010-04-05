package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventFilterTest {    
    /**
     * Test of getMatchedEventIds method, of class EventFilter.
     */
    @Test
    public void testMatchedGetEventIds() {
        EventFilter instance = new EventFilterImpl();
        assertArrayEquals(null, instance.getMatchedEventIds());
    }

    public class EventFilterImpl implements EventFilter {
        @Override
        public String[] getMatchedEventIds() {
            return null;
        }
    }
}
