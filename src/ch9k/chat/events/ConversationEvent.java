package ch9k.chat.events;

import ch9k.chat.Conversation;
import ch9k.eventpool.NetworkEvent;

/**
 * Base event class for all events generated by a Conversation
 * @author Pieter De Baets
 */
public abstract class ConversationEvent extends NetworkEvent {
    private Conversation conversation;

    public ConversationEvent(Conversation conversation) {
        this.conversation = conversation;
    }

    public Conversation getConversation() {
        return conversation;
    }
}
