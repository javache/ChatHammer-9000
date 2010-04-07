package ch9k.chat;

/**
 * Holds the topics for a certain conversation
 * @author Jens Panneel
 */
public class ConversationSubject {
    private String[] subjects;
    private Conversation conversation;

    public ConversationSubject(Conversation conversation, String[] subjects) {
        this.conversation = conversation;
        this.subjects = subjects;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public String[] getSubjects() {
        return subjects;
    }
}
