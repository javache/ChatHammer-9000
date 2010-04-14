package ch9k.network;

import ch9k.eventpool.*;
import java.net.InetAddress;

import org.junit.Test;
import static org.junit.Assert.*;

public class RealLifeTest {
    
    @Test
    public void letTheBeastGo() throws Exception {
        EventPool pool = EventPool.getAppPool();
        pool.raiseEvent(new TestNetworkEvent(InetAddress.getByName("zeus.ugent.be")));
        Thread.sleep(600);
        
    }
    
}
