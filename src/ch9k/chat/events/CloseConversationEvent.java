package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class CloseConversationEvent extends ConversationEvent{

    public CloseConversationEvent(Contact contact) {
        super(contact);
    }

}
