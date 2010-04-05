package ch9k.chat;

import ch9k.plugins.Plugin;
import java.util.List;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation {
    private Contact contact;
    private boolean initiated;
    private List<Plugin> activePlugins;
    private ConversationSubject subject;

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     * @param activePlugins The list of the current active plugins on the initiators side.
     */
    public Conversation(Contact contact, boolean initiatedByMe, List<Plugin> activePlugins) {
        this.contact = contact;
        this.initiated = initiatedByMe;
        this.activePlugins = activePlugins;
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
     * Get a list of all current active plugins from this conversation.
     * @return activePlugins
     */
    public List<Plugin> getActivePlugins() {
        return activePlugins;
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO implement close()
    }

}
