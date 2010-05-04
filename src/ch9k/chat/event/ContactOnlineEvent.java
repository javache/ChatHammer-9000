package ch9k.chat.event;

import ch9k.chat.Contact;

/**
 * This event will be raised en send to every contact when you login.
 * @author Jens Panneel
 */
public class ContactOnlineEvent extends ContactEvent {

    private boolean requiresResponse = false;

    /**
     * Create a new ContactOnlineEvent
     * @param contact
     */
    public ContactOnlineEvent(Contact contact) {
        super(contact);
    }

    public ContactOnlineEvent(Contact contact, boolean requiresResponse) {
        this(contact);
        this.requiresResponse = requiresResponse;
    }

    public boolean requiresReponse() {
        return requiresResponse;
    }

}
