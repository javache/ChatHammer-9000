package ch9k.network;

import org.junit.Test;
import static org.junit.Assert.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import ch9k.eventpool.*;

public class ConnectionTest {

    
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
    
    private class DirectResponseServer implements Runnable {
        public void run() {
            try {
                ServerSocket server = new ServerSocket(1337);
                while(true) {
                    final Socket s = server.accept();
                    new Thread(new Runnable(){
                        public void run() {
                            try {
                                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                                while(true) {
                                    Object obj = in.readObject();
                                    System.out.println("event received on other side");
                                    out.writeObject(obj);
                                }
                            } catch (IOException e) {

                            } catch (ClassNotFoundException e) {

                            }
                        }
                    }).start();
                }
           } catch (IOException e) {

           }
        }
    }
    
    @Test
    public void testSendEvent() {
        EventPool pool = EventPool.getInstance();
        TestListener list = new TestListener();
        pool.addListener(list,new TypeEventFilter(TestNetworkEvent.class));
        new Thread(new DirectResponseServer()).start();
        try {
            Connection conn = new Connection("localhost");
            conn.sendEvent(new TestNetworkEvent());
            Thread.sleep(500);
            assertEquals(1,list.getReceived());
            conn.sendEvent(new TestNetworkEvent());
            Connection conn2 = new Connection("localhost");
            conn.sendEvent(new TestNetworkEvent());
            conn2.sendEvent(new TestNetworkEvent());
            Thread.sleep(1000);
            assertEquals(4,list.getReceived());
        } catch (Exception e) {

        }

        
    }
    
}