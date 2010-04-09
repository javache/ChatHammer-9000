package ch9k.eventpool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Pieter De Baets
 */
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
    public void testGetSource() {
        NetworkEvent instance = new NetworkEvent(testIp);
        assertEquals(null, instance.getSource());
    }

    /**
     * Test of getTarget method, of class NetworkEvent.
     */
    @Test
    public void testGetTarget() {
        NetworkEvent instance = new NetworkEvent(testIp);
        assertEquals(testIp, instance.getTarget());
    }
}
