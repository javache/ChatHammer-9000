package ch9k.eventpool;

import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventHeritageBuilderTest {
    public EventHeritageBuilderTest() {}

    @Test
    public void testGetIds() {
        Collection<String> list;
        
        list = EventHeritageBuilder.getIds(Event.class);
        System.out.println(list);
        assertEquals(1, list.size());

        list = EventHeritageBuilder.getIds(EventChild.class);
        assertEquals(2, list.size());

        list = EventHeritageBuilder.getIds(EventChildChild.class);
        assertEquals(3, list.size());
        String[] expected = {
            "ch9k.eventpool.EventHeritageBuilderTest$EventChildChild",
            "ch9k.eventpool.EventHeritageBuilderTest$EventChild",
            "ch9k.eventpool.Event"
        };
        assertArrayEquals(expected, list.toArray(new String[0]));
    }

    public class EventChild extends Event {}
    public class EventChildChild extends EventChild {}
}
