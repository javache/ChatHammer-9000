package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import ch9k.network.events.NetworkConnectionLostEvent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.junit.Before;
import org.junit.Test;
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

    private class TestListener implements EventListener {
        public int received = 0;

        @Override
        public void handleEvent(Event ev) {
            received++;
        }
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        EventPool pool = new EventPool();
        Thread.sleep(100);
        testListener = new TestListener();
        onlineListener = new OnlineListener();
        pool.addListener(testListener, new TypeEventFilter(TestNetworkEvent.class));
        pool.addListener(onlineListener,new TypeEventFilter(NetworkConnectionLostEvent.class));


        connectionManager = new ConnectionManager(pool);
    }

    @Test
    public void testSendEvent() throws IOException, InterruptedException {
        /**
        DirectResponseServer echoServer = new DirectResponseServer();
        Thread.sleep(100); // wait so port is free
        echoServer.start();

        // number of events to send
        int n = 3;
        for (int i = 0; i < n; i++) {
            connectionManager.sendEvent(new TestNetworkEvent());
        }

        // we should sleep +- 200 ms per event, to make sure they're send
        Thread.sleep(200*n);
        assertEquals(n, testListener.received);

        echoServer.stop();
        Thread.sleep(100); // wait so server has shutdown for sure
        **/

        // what if a contacts pc crashes? 
        // i think this is a better test. I know it fails know, you should fix that :p
        // it also fails some other tests just bij changing this
        DirectResponseServer echoServer = new DirectResponseServer();
        Thread.sleep(100); // wait so port is free
        echoServer.start();

        for (int i = 0; i < 3; i++) {
            connectionManager.sendEvent(new TestNetworkEvent());
        }
        connectionManager.sendEvent(new TestNetworkEvent(InetAddress.getByName("google.be")));
        for (int i = 0; i < 3; i++) {
            connectionManager.sendEvent(new TestNetworkEvent());
        }
        // we should sleep +- 200 ms per event, to make sure they're send
        Thread.sleep(200*6);
        assertEquals(6, testListener.received);

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
        connectionManager.sendEvent(new UnresponsiveTargetEvent());
        // wait long enough
        Thread.sleep(700);
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
