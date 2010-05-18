package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 * This event will be raised when a conversation window is ready for the
 * conversation.
 * @author Jasper Van der Jeugt
 */
public class ConversationWindowReadyEvent extends ConversationEvent {
    /**
     * Constructor.
     * @param conversation Conversation of which the window is ready.
     */
    public ConversationWindowReadyEvent(Conversation conversation) {
        super(conversation);
    }
}
