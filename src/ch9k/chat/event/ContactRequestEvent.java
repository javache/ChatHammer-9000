package ch9k.chat.event;

import ch9k.eventpool.NetworkEvent;
import java.net.InetAddress;

public class ContactRequestEvent extends NetworkEvent {
    
    private String username;

    private String requester;
    
    public ContactRequestEvent(InetAddress target, String username, String requester) {
        super(target);
        this.username = username;
        this.requester = requester;
    }
    
    public String getUsername() {
        return username;
    }

    public String getRequester() {
        return requester;
    }

}
