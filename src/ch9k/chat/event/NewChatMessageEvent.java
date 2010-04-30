package ch9k.chat.event;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;

/**
 * 
 * @author Jens Panneel
 */
public class NewChatMessageEvent extends ConversationEvent {
    private ChatMessage message;

    /**
     * Create a new NewChatMessageEvent
     * @param conversation
     * @param message
     */
    public NewChatMessageEvent(Conversation conversation, ChatMessage message) {
        super(conversation);
        this.message = message;
    }

    /**
     * Get the ChatMessage
     * @return chatMessage
     */
    public ChatMessage getChatMessage() {
        return message;
    }
}
