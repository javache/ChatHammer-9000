package ch9k.chat;

import java.util.Date;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation {
    private Contact contact;
    private boolean initiated;
    private Date starttime;
    private ConversationSubject subject;

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     * @param activePlugins The list of the current active plugins on the initiators side.
     */
    public Conversation(Contact contact, boolean initiatedByMe) {
        this.contact = contact;
        this.initiated = initiatedByMe;
        this.starttime = new Date();
    }

    /**
     * Check whether or not this conversation is started by the current user.
     * @return initiatad
     */
    public boolean initatedByMe() {
        return initiated;
    }

    /**
     * Get the chatting contact on the other end of the line.
     * @return contact
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Get the current subject of this conversation.
     * @return subject
     */
    public ConversationSubject getSubject() {
        return subject;
    }

    /**
     * Set the subject for this conversation.
     * @param subject
     */
    public void setSubject(ConversationSubject subject) {
        this.subject = subject;
    }

    public Date getStartTime() {
        return starttime;
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO implement close() : raise event
    }

}
