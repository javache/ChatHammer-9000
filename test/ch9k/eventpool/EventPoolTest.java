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
        
        pool.addListener(listener, new EventFilter(MyEvent.class));
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
        // create some listeners
        TestListener listener1 = new TestListener();
        TestListener listener2 = new TestListener();
        // and let them listen for MyEvent
        pool.addListener(listener1, new EventFilter(MyEvent.class));
        pool.addListener(listener2, new EventFilter(MyEvent.class));

        // raise an event
        Event event = new MyEvent();
        pool.raiseEvent(event);
        Thread.sleep(50);
        // and the listeners should have received it
        assertEquals(1, listener1.getCount());
        assertEquals(1, listener2.getCount());

        // now remove listener1
        pool.removeListener(listener1);
        pool.raiseEvent(event);
        Thread.sleep(50);
        assertEquals(1, listener1.getCount());
        assertEquals(2, listener2.getCount());
    }

    public class MyEvent extends Event {
        @Override
        public Object getSource() {
            return null;
        }
    }
}