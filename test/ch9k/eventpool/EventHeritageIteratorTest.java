package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventHeritageIteratorTest {
    /**
     * Test of getIds method, of class EventHeritageBuilder.
     */
    @Test
    public void testIterator() {
        EventHeritageIterator iterator;

        iterator = new EventHeritageIterator(Event.class);
        assertEquals(1, iteratorSize(iterator));

        iterator = new EventHeritageIterator(EventChild.class);
        assertEquals(2, iteratorSize(iterator));

        iterator = new EventHeritageIterator(EventChildChild.class);
        String[] expected = {
            "ch9k.eventpool.EventHeritageIteratorTest$EventChildChild",
            "ch9k.eventpool.EventHeritageIteratorTest$EventChild",
            "ch9k.eventpool.Event"
        };
        for(String className : expected) {
            assertEquals(className, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    private int iteratorSize(EventHeritageIterator iterator) {
        int inheritanceCount = 0;
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
            inheritanceCount++;
        }
        return inheritanceCount;
    }

    public class EventChild extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
    
    public class EventChildChild extends EventChild {}
}
