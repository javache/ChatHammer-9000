package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class ContactOfflineEvent extends ContactStatusEvent {

    public ContactOfflineEvent(Contact contact) {
        super(contact);
    }
}
