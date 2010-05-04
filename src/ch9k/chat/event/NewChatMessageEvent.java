package ch9k.chat.event;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;

/**
 * 
 * @author Jens Panneel
 */
public class NewChatMessageEvent extends ConversationEvent {
    private ChatMessage message;
    private boolean systemEvent;

    /**
     * Create a new NewChatMessageEvent
     * @param conversation
     * @param message
     */
    public NewChatMessageEvent(Conversation conversation, ChatMessage message) {
        this(conversation, message, false);
    }

    /**
     * Create a new NewChatMessageEvent
     * @param conversation
     * @param message
     * @param systemEvent
     */
    public NewChatMessageEvent(Conversation conversation, ChatMessage message,
            boolean systemEvent) {
        super(conversation);
        this.message = message;
        this.systemEvent = systemEvent;
    }


    /**
     * Get the ChatMessage
     * @return chatMessage
     */
    public ChatMessage getChatMessage() {
        return message;
    }
    
    /**
     * Get the value of systemEvent
     *
     * @return the value of systemEvent
     */
    public boolean isSystemMessage() {
        return systemEvent;
    }
}
