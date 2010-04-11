package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when you block a contact.
 * @author Jens Panneel
 */
public class ContactBlockedEvent extends ContactStatusEvent {

    /**
     * Create a new ContactBlockedEvent
     * @param contact
     */
    public ContactBlockedEvent(Contact contact) {
        super(contact);
    }

}
