package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import java.io.IOException;
import java.net.InetAddress;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
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
    public void setUp() throws IOException {
        pool = new EventPool();
        testListener = new TestListener();
        pool.addListener(testListener, new TypeEventFilter(TestNetworkEvent.class));

        echoServer = new DirectResponseServer();
        echoServer.start();
    }

    @After
    public void tearDown() {
        echoServer.stop();
    }

    /**
     * Test of sendEvent method, of class Connection
     * @throws IOException
     * @throws InterruptedException 
     */
    @Test
    public void testSendEvent() throws IOException, InterruptedException {
        Connection conn = new Connection(InetAddress.getLocalHost(), pool);
        Connection conn2 = new Connection(InetAddress.getLocalHost(), pool);

        conn.sendEvent(new TestNetworkEvent());
        Thread.sleep(50);
        assertEquals(1, testListener.received);

        conn.sendEvent(new TestNetworkEvent());
        conn.sendEvent(new TestNetworkEvent());
        conn2.sendEvent(new TestNetworkEvent());
        Thread.sleep(150);
        assertEquals(4, testListener.received);
    }
}
