package ch9k.chat;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation {
    private Contact contact;
    private boolean initiated;
    private Date starttime;
    private ConversationSubject subject;
    private Set<ChatMessage> conversation;

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     * @param activePlugins The list of the current active plugins on the initiators side.
     */
    public Conversation(Contact contact, boolean initiatedByMe) {
        this.starttime = new Date();
        this.conversation = new TreeSet<ChatMessage>();
        this.contact = contact;
        this.initiated = initiatedByMe;
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

    /**
     * Get the date/time on witch this conversation was started
     * @return starttime
     */
    public Date getStartTime() {
        return starttime;
    }

    /**
     * Adds a message to this Conversation.
     * @param chatMessage
     */
    public void addChatMessage(ChatMessage chatMessage) {
        conversation.add(chatMessage);
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO implement close() : raise event
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Conversation other = (Conversation) obj;
        if (this.contact != other.contact && (this.contact == null || !this.contact.equals(other.contact))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.contact != null ? this.contact.hashCode() : 0);
        return hash;
    }
    
}
