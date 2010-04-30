package ch9k.chat.event;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

/**
 * An EventFilter that is only interested in the type of the events
 * @author Pieter De Baets
 */
public class ConversationEventFilter extends EventFilter {
    private Conversation conversation;

    /**
     * Construct a new ConversationEventFilter
     * @param conversation
     */
    public ConversationEventFilter(Conversation conversation) {
        super(ConversationEvent.class);
        this.conversation = conversation;
    }

    /**
     * Construct a new ConversationEventFilter that filters subclasses
     * @param type
     * @param conversation
     */
    public ConversationEventFilter(Class<? extends ConversationEvent> type, Conversation conversation) {
        super(type);
        this.conversation = conversation;
    }



    @Override
    public boolean accept(Event event) {
        if(super.accept(event)) {
            ConversationEvent conversationEvent = (ConversationEvent) event;
            return conversation.equals(conversationEvent.getConversation());
        }
        return false;
    }
}
