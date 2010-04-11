package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This class is intended to group underlaying classes.
 * @author Jens Panneel
 */
public abstract class ContactStatusEvent extends ConversationEvent {

    /**
     * Create a new ContactStatusEvent
     * @param contact
     */
    public ContactStatusEvent(Contact contact) {
        super(contact);
    }

}
