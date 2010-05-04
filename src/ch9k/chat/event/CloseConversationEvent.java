package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 * This event will be raised when a conversation window is closed
 * @author Jens Panneel
 */
public class CloseConversationEvent extends ConversationEvent{
    public CloseConversationEvent(Conversation conversation) {
        super(conversation);
    }
}
