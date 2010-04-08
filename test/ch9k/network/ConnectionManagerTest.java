package ch9k.network;

import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import org.junit.Test;
import java.net.Socket;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.io.IOException;
import static org.junit.Assert.*;

public class ConnectionManagerTest {
    private class TestListener implements EventListener {
        private int received;
        
        public TestListener() {
            received = 0;
        }
        
        public void handleEvent(Event ev){
            received++;
        }
        
        public int getReceived(){
            return received;
        }
        
    }

    @Test
    public void testSendEvent() throws IOException {
        ConnectionManager connMan = new ConnectionManager(EventPool.getAppPool());
        TestListener list = new TestListener();
        EventPool.getAppPool().addListener(list,new TypeEventFilter(TestNetworkEvent.class));
        DirectResponseServer server = new DirectResponseServer();
        server.start();
        
        // the number of events to send
        int n = 3;

        for (int i = 0; i < n; i++) {
            connMan.sendEvent(new TestNetworkEvent());            
        }

        // we should sleep +- 10 ms per event, to make sure they're send
        try {    
            Thread.sleep(10*n);
        } catch (InterruptedException e) {
            
        }
        server.stop();        
        assertEquals(n,list.getReceived());
    }
    
    @Test
    public void testShouldRaiseConnectException() {
        boolean raised = false;
        ConnectionManager connMan = new ConnectionManager(EventPool.getAppPool());
        try {
            Socket s = new Socket("localhost",Connection.DEFAULT_PORT);
        } catch (UnknownHostException e) {
            
        } catch (ConnectException e){
            raised = true;
        } catch (IOException e) {
            
        }
        assertEquals(true,raised);
    }
    
    @Test
    public void testShouldNotRaiseConnectException() throws ConnectException,IOException {
        ConnectionManager connMan = new ConnectionManager(EventPool.getAppPool());
        connMan.readyForIncomingConnections();
        try {
            Socket s = new Socket("localhost",Connection.DEFAULT_PORT);
        } catch (UnknownHostException e) {

        } 
    }
    
}