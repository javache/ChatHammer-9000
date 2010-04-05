package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pieter De Baets
 */
public class TypeEventFilterTest {
    /**
     * Test of getMatchedEventIds method, of class TypeEventFilter.
     */
    @Test
    public void testGetMatchedEventIds() {
        TypeEventFilter instance = new TypeEventFilter(Event.class);
        assertArrayEquals(new String[] { "ch9k.eventpool.Event" },
                instance.getMatchedEventIds());
    }
}
