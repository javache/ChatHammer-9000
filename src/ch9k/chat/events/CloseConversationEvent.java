package ch9k.chat.events;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class CloseConversationEvent extends ConversationEvent{

    public CloseConversationEvent(Conversation conversation) {
        super(conversation);
    }

}
