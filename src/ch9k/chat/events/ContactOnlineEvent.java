package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised en send to every contact when you login.
 * @author Jens Panneel
 */
public class ContactOnlineEvent extends ContactStatusEvent {

    /**
     * Create a new ContactOnlineEvent
     * @param contact
     */
    public ContactOnlineEvent(Contact contact) {
        super(contact);
    }

}
