package ch9k.chat.events;

import ch9k.chat.Contact;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

/**
 * An EventFilter that is only interested in the type of the events
 * @author Pieter De Baets
 */
public class ConversationEventFilter implements EventFilter {
    private Contact contact;

    /**
     * Construct a new TypeEventFilter
     * @param conversation
     */
    public ConversationEventFilter(Contact contact) {
        this.contact = contact;
    }

    @Override
    public boolean accept(Event event) {
        if(!(event instanceof ConversationEvent)) {
            return false;
        } else {
            return contact.equals(((NewChatMessageEvent)event).getContact());
        }
    }
}
