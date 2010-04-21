package ch9k.eventpool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NetworkEventTest {
    private InetAddress testIp;

    @Before
    public void setUp() throws UnknownHostException {
        testIp = InetAddress.getByName("thinkjavache.be");
    }
    
    /**
     * Test of getSource method, of class NetworkEvent.
     */
    @Test
    public void testGetSetSource() {
        NetworkEvent instance = new NetworkEvent(testIp);
        assertEquals(null, instance.getSource());
        instance.setSource(testIp);
        assertEquals(testIp, instance.getSource());
    }

    /**
     * Test of getTarget method, of class NetworkEvent.
     */
    @Test
    public void testGetTarget() {
        NetworkEvent instance = new NetworkEvent(testIp);
        assertEquals(testIp, instance.getTarget());
    }

    /**
     * Test of isExternal method, of class NetworkEvent.
     */
    @Test
    public void testIsExternal() {
        NetworkEvent instance = new NetworkEvent(testIp);
        assertFalse(instance.isExternal());
        instance.setSource(testIp);
        assertTrue(instance.isExternal());
    }
}
