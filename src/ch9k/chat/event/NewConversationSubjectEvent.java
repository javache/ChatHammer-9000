package ch9k.chat.event;

import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;

/**
 *
 * @author Jens Panneel
 */
public class NewConversationSubjectEvent extends ConversationEvent {
    private ConversationSubject conversationSubject;

    public NewConversationSubjectEvent(Conversation conversation, ConversationSubject conversationSubject) {
        super(conversation);
        this.conversationSubject = conversationSubject;
    }

    public ConversationSubject getConversationSubject() {
        return conversationSubject;
    }

}
