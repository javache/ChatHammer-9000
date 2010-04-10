package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TestListener;
import ch9k.eventpool.TypeEventFilter;
import ch9k.network.events.NetworkConnectionLostEvent;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ConnectionManagerTest {
    private TestListener testListener;
    private OnlineListener onlineListener;

    private ConnectionManager connectionManager;

    private class OnlineListener implements EventListener {
        public boolean online = true;
        
        @Override
        public void handleEvent(Event ev) {
            online = false;
        }
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        EventPool pool = new EventPool();
        testListener = new TestListener();
        onlineListener = new OnlineListener();
        pool.addListener(testListener, new TypeEventFilter(TestNetworkEvent.class));
        pool.addListener(onlineListener, new TypeEventFilter(NetworkConnectionLostEvent.class));

        connectionManager = new ConnectionManager(pool);
    }

    @Test
    public void testSendEvent() throws IOException, InterruptedException {
        DirectResponseServer echoServer = new DirectResponseServer();
        Thread.sleep(100); // wait so port is free
        echoServer.start();

        for (int i = 0; i < 3; i++) {
            connectionManager.sendEvent(new TestNetworkEvent());
        }
        connectionManager.sendEvent(new TestNetworkEvent("google.be"));
        for (int i = 0; i < 3; i++) {
            connectionManager.sendEvent(new TestNetworkEvent());
        }
        
        // sleep a good while, so everything is sent
        Thread.sleep(1000);
        assertEquals(6, testListener.receiveCount);

        echoServer.stop();
        Thread.sleep(100); // wait so server has shutdown for sure
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDisconnect() throws IOException, InterruptedException {
        connectionManager.readyForIncomingConnections();
        Thread.sleep(100);

        Connection conn = new Connection(InetAddress.getLocalHost(), new EventPool());
        assertTrue(conn.hasConnection());

        // close it up
        connectionManager.disconnect();
        Thread.sleep(100);
        assertFalse(conn.hasConnection());

        // we shouldn't be able to connect any more
        boolean exceptionThrown = false;
        try {
            conn = new Connection(InetAddress.getLocalHost(), new EventPool());
        } catch(IOException ex) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
    
    @Test
    public void shouldNotThinkConnectionIsLost() throws Exception {
        connectionManager.sendEvent(new TestNetworkEvent("google.be"));
        // wait long enough
        Thread.sleep(600);
        assertTrue(onlineListener.online);
    }
    
    @Test(expected=ConnectException.class)
    public void testShouldRaiseConnectException() throws IOException {
        Socket s = new Socket(InetAddress.getLocalHost(), Connection.DEFAULT_PORT);
    }
    
    @Test
    public void testShouldNotRaiseConnectException() throws IOException, InterruptedException {
        connectionManager.readyForIncomingConnections();
        // creating a serversocket takes some time, lets wait a bit
        Thread.sleep(100);
        Socket s = new Socket(InetAddress.getLocalHost(), Connection.DEFAULT_PORT);
        s.close();
    }
}
