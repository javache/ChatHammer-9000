package ch9k.chat;

import java.io.Serializable;

/**
 * Holds the topics for a certain conversation
 * @author Jens Panneel
 */
public class ConversationSubject implements Serializable {
    private String[] subjects;

    /**
     * Constructor
     * @param conversation
     * @param subjects
     */
    public ConversationSubject(String[] subjects) {
        this.subjects = subjects;
    }

    /**
     * Get the subject returned as String[]
     * @return
     */
    public String[] getSubjects() {
        return subjects;
    }
}
