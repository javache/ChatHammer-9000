package ch9k.chat.event;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.core.ChatApplication;
import ch9k.eventpool.AbstractEvent;

/**
 * Base event class for all a simple, local conversation event.
 * @author Jasper Van der Jeugt
 */
public abstract class LocalConversationEvent extends AbstractEvent {
    /**
     * The actual conversation.
     */
    private Conversation conversation;

    /**
     * Create a new ConversionEvent from a conversation
     * @param source Event source.
     * @param conversation Associated conversation.
     */
    public LocalConversationEvent(Object source, Conversation conversation) {
        super(source);
        this.conversation = conversation;
    }

    /**
     * Get the conversation.
     * @return The conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }
}
