package ch9k.eventpool;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
public class NetworkEventTest {
    /**
     * Test of getSource method, of class NetworkEvent.
     */
    @Test
    public void testGetSource() {
        NetworkEvent instance = new NetworkEvent();
        assertEquals(null, instance.getSource());
    }

    /**
     * Test of getTarget method, of class NetworkEvent.
     */
    @Test
    public void testGetTarget() {
        NetworkEvent instance = new NetworkEvent();
        assertEquals(null, instance.getTarget());
    }
}
