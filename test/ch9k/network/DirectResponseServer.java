package ch9k.network;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * This is a test helper class
 * When it receives an object on DEFAULT_PORT,
 * it will resend it to the sender
 */
public class DirectResponseServer {    
    private ServerSocket server;
    private List<Socket> clients;
    
    private static final Logger logger =
            Logger.getLogger(DirectResponseServer.class);
    
    public DirectResponseServer() throws IOException {
        server = new ServerSocket(Connection.DEFAULT_PORT);
        clients = new ArrayList<Socket>();
    }
    
    public void stop() {
        try {
            for(Socket client : clients) {
                client.close();
            }
            server.close();
        } catch (IOException ex) {
            logger.warn(ex.toString());
        }
    }
    
    public void start() {
        new Thread(new Runnable() {
            public void run() {
                runServer();
            }
        }).start();
    }

    private void runServer() {
        while(!server.isClosed()) {
            try {
                final Socket s = server.accept();
                clients.add(s);

                new Thread(new Runnable() {
                    public void run() {
                        runClient(s);
                    }
                }).start();
            } catch (IOException ex) {
                logger.warn(ex.toString());
            }
        }
    }

    private void runClient(Socket s) {
        try {
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            while(!s.isClosed()) {
                Object obj = in.readObject();
                out.writeObject(obj);
            }
        } catch (ClassNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
}
