package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * @author Pieter De Baets
 */
public class EventPoolTest {
    /**
     * Test of raiseEvent method, of class EventPool.
     */
    @Test
    public void testBasicRaiseEvent() {
        EventPool pool = new EventPool();
        Event event = new MyEvent();

        EventListener listener = createMock(EventListener.class);
        listener.handleEvent(event);
        replay(listener); // the previous calls to listener should now be repeated

        pool.addListener(listener, new TypeEventFilter(MyEvent.class));
        pool.raiseEvent(event);
        verify(listener); // assert that the event was received
    }

    public class MyEvent extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}