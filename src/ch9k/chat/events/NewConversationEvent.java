package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be sent when we start a new Conversation
 * @author nudded
 */
public class NewConversationEvent extends ConversationEvent {
    /**
     * Create a new NewConversationEvent
     * @param conversation
     */
    public NewConversationEvent(Contact contact) {
        // really ugly hierarchy, i could make it beter when i have time ;)
        super(contact);
    } 
}
