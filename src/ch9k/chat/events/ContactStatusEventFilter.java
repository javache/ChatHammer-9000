package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

/**
 * This will filter the events only needed for a given contact
 * @author Jens Panneel
 */
public class ContactStatusEventFilter implements EventFilter{
    private Contact contact;

    /**
     * Construct a new ContactStatusEventFilter
     * @param contact
     */
    public ContactStatusEventFilter(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean accept(Event event) {
        if(!(event instanceof ContactStatusEvent)) {
            return false;
        } else {
            ContactStatusEvent contactStatusEvent = (ContactStatusEvent) event;
            return contactStatusEvent.getContact().equals(contact);
        }
    }

}
