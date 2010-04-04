package ch9k.eventpool;

import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventHeritageBuilderTest {
    /**
     * Test of getIds method, of class EventHeritageBuilder.
     */
    @Test
    public void testGetIds() {
        Collection<String> list;
        EventHeritageBuilder instance = new EventHeritageBuilder();
        
        list = instance.getIds(Event.class);
        System.out.println(list);
        assertEquals(1, list.size());

        list = instance.getIds(EventChild.class);
        assertEquals(2, list.size());

        list = instance.getIds(EventChildChild.class);
        assertEquals(3, list.size());
        String[] expected = {
            "ch9k.eventpool.EventHeritageBuilderTest$EventChildChild",
            "ch9k.eventpool.EventHeritageBuilderTest$EventChild",
            "ch9k.eventpool.Event"
        };
        assertArrayEquals(expected, list.toArray(new String[0]));
    }

    public class EventChild extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
    
    public class EventChildChild extends EventChild {}
}
