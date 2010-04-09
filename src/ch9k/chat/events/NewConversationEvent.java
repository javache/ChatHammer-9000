package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be sent when we start a new Conversation
 * or it will be received
 * @author nudded
 */

public class NewConversationEvent extends ConversationEvent {

 
    public NewConversationEvent(Contact contact) {
        super(contact);
    } 
}