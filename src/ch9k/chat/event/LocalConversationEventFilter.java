package ch9k.chat.event;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;

public class LocalConversationEventFilter extends EventFilter {
    /**
     * Conversation to match.
     */
    private Conversation conversation;

    /**
     * Construct a new ConversationEventFilter that filters subclasses
     * @param type
     * @param conversation
     */
    public LocalConversationEventFilter(
            Class<? extends LocalConversationEvent> type,
            Conversation conversation) {
        super(type);
        this.conversation = conversation;
    }

    @Override
    public boolean accept(Event e) {
        if(super.accept(e)) {
            LocalConversationEvent event = (LocalConversationEvent) e;
            return conversation.equals(event.getConversation());
        }

        return false;
    }
}
