package ch9k.network;

import ch9k.eventpool.DataEvent;
import java.net.InetAddress;

public class TestDataEvent extends DataEvent {
    public String largeData = "lol, not really";
    
    public TestDataEvent(InetAddress target) {
        super(target);
    }
}
