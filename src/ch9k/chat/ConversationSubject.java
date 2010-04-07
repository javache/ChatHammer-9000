package ch9k.chat;

/**
 * Holds the topics for a certain conversation
 * @author Jens Panneel
 */
public class ConversationSubject {
    private String[] subjects;
    private Conversation conversation;

    /**
     * Constructor
     * @param conversation
     * @param subjects
     */
    public ConversationSubject(Conversation conversation, String[] subjects) {
        this.conversation = conversation;
        this.subjects = subjects;
    }

    /**
     * Get the conversation from which this is the subject
     * @return
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Get the subject returned as String[]
     * @return
     */
    public String[] getSubjects() {
        return subjects;
    }
}
