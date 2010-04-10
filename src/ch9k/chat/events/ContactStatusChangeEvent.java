package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class ContactStatusChangeEvent extends ContactStatusEvent {
    private String status;

    public ContactStatusChangeEvent(Contact contact, String status) {
        super (contact);
        this.status = status;
    }

    public String getNewStatus() {
        return status;
    }

}
