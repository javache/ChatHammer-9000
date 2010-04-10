package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class ContactOnlineEvent extends ContactStatusEvent {

    public ContactOnlineEvent(Contact contact) {
        super(contact);
    }

}
