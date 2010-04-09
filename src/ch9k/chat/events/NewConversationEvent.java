package ch9k.chat.events;

import ch9k.chat.Conversation;

/**
 * This event will be sent when we start a new Conversation
 * @author nudded
 */
public class NewConversationEvent extends ConversationEvent {
    /**
     * Create a new NewConversationEvent
     * @param conversation
     */
    public NewConversationEvent(Conversation conversation) {
        super(conversation);
    } 
}
