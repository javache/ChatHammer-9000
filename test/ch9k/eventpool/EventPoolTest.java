package ch9k.eventpool;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class EventPoolTest {
    private EventPool pool;

    @Before
    public void setUp() {
        pool = new EventPool();
    }

    /**
     * Test of raiseEvent method, of class EventPool.
     * @throws InterruptedException 
     */
    @Test
    public void testRaiseEvent() throws InterruptedException {
        Event event = new MyEvent();
        TestListener listener = new TestListener();
        
        pool.addListener(listener, new TypeEventFilter(MyEvent.class));
        pool.raiseEvent(event);
        Thread.sleep(50); // wait for the event to be propagated

        // assert that the event was received
        assertEquals(1, listener.receiveCount);
        assertEquals(event, listener.lastReceivedEvent);
    }

    /**
     * Test of removeListener method, of class EventPool.
     * @throws InterruptedException
     */
    @Test
    public void testRemoveListener() throws InterruptedException {
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        Event event = new MyEvent();
        assertTrue(listener1.getCount() == 0);
        assertTrue(listener1.getCount() == 0);
        pool.addListener(listener1, new TypeEventFilter(MyEvent.class));
        pool.addListener(listener2, new TypeEventFilter(MyEvent.class));
        pool.raiseEvent(event);
        Thread.sleep(100);
        assertTrue(listener1.getCount() == 1);
        assertTrue(listener2.getCount() == 1);
        pool.removeListener(listener1);
        pool.raiseEvent(event);
        Thread.sleep(100);
        assertTrue(listener1.getCount() == 1);
        assertTrue(listener2.getCount() == 2);
    }

    public class MyEvent extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}