package ch9k.chat.events;

import ch9k.chat.Contact;

import ch9k.eventpool.NetworkEvent

/**
 * This event will be sent when we start a new Conversation
 * or it will be received
 * @author nudded
 */

public class NewConversationEvent extends NetworkEvent {
    
    private Contact contact;
    
    public NewConversationEvent(Contact contact) {
        this.contact = contact;
    }
    
    public Contact getSource() {
        return contact;
    }
    
}