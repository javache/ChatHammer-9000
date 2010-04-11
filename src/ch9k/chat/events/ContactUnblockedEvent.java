package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when you unblock a contact
 * @author Jens Panneel
 */
public class ContactUnblockedEvent extends ContactStatusEvent {

    /**
     * Create a new ContactUnblockedEvent
     * @param contact
     */
    public ContactUnblockedEvent(Contact contact) {
        super(contact);
    }

}
