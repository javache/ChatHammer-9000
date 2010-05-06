package ch9k.chat.event;

import ch9k.chat.Contact;
import ch9k.core.ChatApplication;

/**
 * This event will be raised en send to every contact when you login.
 * @author Jens Panneel
 */
public class ContactOnlineEvent extends ContactEvent {
    private boolean requiresResponse = false;
    private String status;

    /**
     * Create a new ContactOnlineEvent
     * @param contact
     */
    public ContactOnlineEvent(Contact contact) {
        super(contact);
        status = ChatApplication.getInstance().getAccount().getStatus();
    }

    public ContactOnlineEvent(Contact contact, boolean requiresResponse) {
        this(contact);
        this.requiresResponse = requiresResponse;
    }

    public boolean requiresReponse() {
        return requiresResponse;
    }

    public String getStatus() {
        return status;
    }
}
