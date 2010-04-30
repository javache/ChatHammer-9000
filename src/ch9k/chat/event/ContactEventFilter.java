package ch9k.chat.event;

import ch9k.chat.Contact;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

/**
 * This will filter the events only needed for a given contact
 * @author Jens Panneel
 */
public class ContactEventFilter extends EventFilter{
    private Contact contact;

    /**
     * Construct a new ContactStatusEventFilter
     * @param contact
     */
    public ContactEventFilter(Contact contact) {
        super(ContactEvent.class);
        this.contact = contact;
    }

    @Override
    public boolean accept(Event event) {
        if(super.accept(event)) {
            ContactEvent contactEvent = (ContactEvent) event;
            return contact.equals(contactEvent.getContact());
        }
        return false;
    }
}
