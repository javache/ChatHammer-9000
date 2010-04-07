package ch9k.network;

import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.TestCase;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import ch9k.eventpool.*;

public class ConnectionManagerTest extends TestCase {
    
    
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
    public void testSendEvent() {
        ConnectionManager connMan = new ConnectionManager();
        TestListener list = new TestListener();
        EventPool.getInstance().addListener(list,new TypeEventFilter(TestNetworkEvent.class));
        DirectResponseServer server = new DirectResponseServer();
        server.start();
        
        connMan.sendEvent(new TestNetworkEvent());
        try {    
            Thread.sleep(500);
        } catch (InterruptedException e) {
            
        }
        server.stop();        
        assertEquals(1,list.getReceived());
    }
    
    @Test(expected=ConnectException.class)
    public void testShouldRaiseConnectException() throws IOException {
        ConnectionManager connMan = new ConnectionManager();
        try {
            Socket s = new Socket("localhost",Connection.DEFAULT_PORT);
        } catch (UnknownHostException e) {
            
        } catch (IOException e) {
            if (e.getClass() == ConnectException.class) {
                throw e;
            }
        }
    }
    
    @Test
    public void testShouldNotRaiseConnectException() throws ConnectException,IOException {
        ConnectionManager connMan = new ConnectionManager();
        connMan.readyForIncomingConnections();
        try {
            Socket s = new Socket("localhost",Connection.DEFAULT_PORT);
        } catch (UnknownHostException e) {

        } 
    }
    
}