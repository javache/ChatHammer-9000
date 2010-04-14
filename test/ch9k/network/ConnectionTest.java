package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import java.io.IOException;
import java.net.InetAddress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectionTest {
    private EventPool pool;
    private TestListener testListener;
    private DirectResponseServer echoServer;

    private class TestListener implements EventListener {
        public int received = 0;

        @Override
        public void handleEvent(Event ev) {
            received++;
        }
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        pool = new EventPool();
        testListener = new TestListener();
        pool.addListener(testListener, new EventFilter(TestNetworkEvent.class));

        echoServer = new DirectResponseServer();
        echoServer.start();
    }

    @After
    public void tearDown() throws InterruptedException {
        echoServer.stop();
        Thread.sleep(50);
    }

    /**
     * Test of sendEvent method, of class Connection
     * @throws IOException
     * @throws InterruptedException 
     */
    @Test
    public void testSendEvent() throws IOException, InterruptedException {
        Connection conn1 = new Connection(InetAddress.getLocalHost(), pool, null);
        Connection conn2 = new Connection(InetAddress.getLocalHost(), pool, null);

        conn1.sendEvent(new TestNetworkEvent());
        Thread.sleep(200);
        assertEquals(1, testListener.received);

        conn1.sendEvent(new TestNetworkEvent());
        conn1.sendEvent(new TestNetworkEvent());
        conn2.sendEvent(new TestNetworkEvent());
        Thread.sleep(200);
        assertEquals(4, testListener.received);
    }

    /**
     * Test of hasConnection methode, of class Connection
     * @throws IOException
     */
    //@Test
    public void testHasConnection() throws IOException, InterruptedException {
        Connection conn = new Connection(InetAddress.getLocalHost(), pool, null);
        Thread.sleep(100);
        assertTrue(conn.hasConnection());

        echoServer.stop(); // connection should now be broken
        Thread.sleep(100);
        assertFalse(conn.hasConnection());
    }

    /**
     * Test of close methode, of class Connection
     * @throws IOException 
     */
    //@Test
    public void testClose() throws IOException {
        Connection conn = new Connection(InetAddress.getLocalHost(), pool, null);
        assertTrue(conn.hasConnection());

        conn.close();
        assertFalse(conn.hasConnection());
    }
}
