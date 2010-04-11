package ch9k.chat.events;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

/**
 * An EventFilter that is only interested in the type of the events
 * @author Pieter De Baets
 */
public class ConversationEventFilter implements EventFilter {
    private Conversation conversation;

    /**
     * Construct a new ConversationEventFilter
     * @param conversation
     */
    public ConversationEventFilter(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public boolean accept(Event event) {
        if(!(event instanceof ConversationEvent)) {
            return false;
        } else {
            return conversation.equals(((ConversationEvent)event).getConversation());
        }
    }
}
