package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

public class EventFilterTest {
    /**
     * Test of getMatchedEventIds method, of class EventFilter.
     */
    @Test
    public void testAccept() {
        EventFilter instance = new EventFilter(EventA.class);
        assertTrue(instance.accept(new EventA()));
        assertFalse(instance.accept(new EventB()));
        assertTrue(instance.accept(new EventAChild()));
    }

    public class EventA extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }

    public class EventAChild extends EventA {}
    
    public class EventB extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}
