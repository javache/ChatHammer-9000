package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when you go offline.
 * @author Jens Panneel
 */
public class ContactOfflineEvent extends ContactStatusEvent {

    /**
     * Create a new ContactOfflineEvent
     * @param contact
     */
    public ContactOfflineEvent(Contact contact) {
        super(contact);
    }
}
