package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class ContactStatusEvent extends ConversationEvent {

    public ContactStatusEvent(Contact contact) {
        super(contact);
    }

}
