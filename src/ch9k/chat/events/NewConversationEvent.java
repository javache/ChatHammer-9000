package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when we start a new Conversation
 * @author Jens Panneel
 */
public class NewConversationEvent extends ConversationEvent {
    /**
     * Constructor
     * @param contaxt
     */
    public NewConversationEvent(Contact contact) {
        // really ugly hierarchy, i could make it beter when i have time ;)
        super(contact);
    } 
}
