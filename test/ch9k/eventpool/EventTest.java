package ch9k.eventpool;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class EventTest {
    /**
     * Test of isHandledsetHandled method, of class Event.
     */
    @Test
    public void testHandled() {
        Event instance = new EventImpl();
        assertFalse(instance.isHandled());
        instance.setHandled(true);
        assertTrue(instance.isHandled());
    }
    
    /**
     * Test of getCreatedAt method, of class Event.
     */
    @Test
    public void testGetCreatedAt() throws InterruptedException {
        Event instance = new EventImpl();
        assertNotNull(instance.getCreatedAt());
        Thread.sleep(25);
        assertTrue(instance.getCreatedAt().before(new Date()));
    }

    public class EventImpl extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}
