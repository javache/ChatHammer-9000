package ch9k.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

/**
 * This is a test helper class
 * When it receives an object on DEFAULT_PORT,
 * it will resend it to the sender
 */
public class DirectResponseServer {
    
    private boolean shouldRun;
    
    private ServerSocket server;
    
    public DirectResponseServer() throws IOException {
        shouldRun = true;
        server = new ServerSocket(Connection.DEFAULT_PORT);
    }
    
    public void stop() {
        shouldRun = false;
        try {
            server.close();
        } catch (IOException e) {
            
        } catch (NullPointerException e) {
            
        }
    }
    
    public void start() {
        new Thread(new Runner()).start();
    }
    
    private class Runner implements Runnable {
        public void run() {
            try {
                while(shouldRun) {
                    final Socket s = server.accept();
                    new Thread(new Runnable(){
                        public void run() {
                            try {
                                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                                while(true) {
                                    Object obj = in.readObject();
                                    out.writeObject(obj);
                                }
                            } catch (IOException e) {

                            } catch (ClassNotFoundException e) {

                            }
                        }
                    }).start();
                }
                server.close();
            } catch (IOException e) {
                    
            }
        }
    }
}
