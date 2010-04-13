package ch9k.chat.events;

import ch9k.chat.Contact;

/**
 * This event will be raised when you change your status.
 * @author Jens Panneel
 */
public class ContactStatusEvent extends ContactEvent {
    private String status;

    /**
     * Create a new ContactStatusChangeEvent
     * @param contact
     * @param status 
     */
    public ContactStatusEvent(Contact contact, String status) {
        super (contact);
        this.status = status;
    }

    /**
     * Get the new status.
     * @return status
     */
    public String getNewStatus() {
        return status;
    }
}
