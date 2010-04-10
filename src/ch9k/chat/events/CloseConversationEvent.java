package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when a conversation window is closed
 * @author Jens Panneel
 */
public class CloseConversationEvent extends ConversationEvent{

    public CloseConversationEvent(Contact contact) {
        super(contact);
    }

}
