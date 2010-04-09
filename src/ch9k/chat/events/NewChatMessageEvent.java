package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Contact;

/**
 *
 * @author Jens Panneel
 */
public class NewChatMessageEvent extends ConversationEvent {
    private ChatMessage chatMessage;

    public NewChatMessageEvent(ChatMessage chatMessage, Contact receiver) {
        super(receiver);
        this.chatMessage = chatMessage;
    }

    /**
     * Get the ChatMessage
     * @return chatMessage
     */
    public ChatMessage getChatMessage() {
        return chatMessage;
    }

}
