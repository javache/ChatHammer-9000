package ch9k.chat.events;

import ch9k.chat.ChatMessage;
import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class NewChatMessageEvent extends ConversationEvent {
    private ChatMessage chatMessage;

    public NewChatMessageEvent(ChatMessage chatMessage, Conversation conversation) {
        super(conversation);
        this.chatMessage = chatMessage;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

}
