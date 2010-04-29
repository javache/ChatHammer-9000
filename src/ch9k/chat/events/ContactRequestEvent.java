package ch9k.chat.events;

import java.net.InetAddress;
import ch9k.eventpool.NetworkEvent;

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