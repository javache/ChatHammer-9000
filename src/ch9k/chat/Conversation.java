package ch9k.chat;

import ch9k.plugins.Plugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a conversation between two users.
 * @author Jens Panneel
 */
public class Conversation {
    private Contact contact;
    private boolean initiated;
    private Map<Class<? extends Plugin>, Plugin> activePlugins;
    private ConversationSubject subject;

    /**
     * Constructor
     * @param contact The contact you are chatting with.
     * @param initiatedByMe Is this conversation started by me?
     * @param activePlugins The list of the current active plugins on the initiators side.
     */
    public Conversation(Contact contact, boolean initiatedByMe, List<Class<? extends Plugin>> activePlugins) {
        this.contact = contact;
        this.initiated = initiatedByMe;
        this.activePlugins = new HashMap<Class<? extends Plugin>, Plugin>();
        for(Class<? extends Plugin> plugin : activePlugins) {
            try {
                addPlugin(plugin);
            } catch (Exception ex) {
                // TODO catch it here
            }
        }
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
     * Get a list of all current active plugins from this conversation.
     * @return activePlugins
     */
    public Set<Plugin> getActivePlugins() {
        return (Set<Plugin>) activePlugins.values();
    }

    /**
     * Delete the instance of this plugin from this conversation
     * @param plugin Class<? extends Plugin>
     */
    public void deletePlugin(Class<? extends Plugin> plugin) {
        activePlugins.remove(plugin);
    }

    /**
     * Add an instance of plugin to this conversation, throws exception when there is already such an instance
     * @param plugin Class<? extends Plugin>
     * @throws InstantiationException
     * @throws IllegalAccessException
     * will throw plugin conflict exception
     */
    public void addPlugin(Class<? extends Plugin> plugin) throws InstantiationException, IllegalAccessException {
        if(!activePlugins.keySet().contains(plugin)) {
            activePlugins.put(plugin, plugin.newInstance());
        } else {
            // TODO throw plugin conflict exception
        }
    }

    /**
     * Close this conversation
     */
    public void close() {
        // TODO implement close() : raise event
    }

}
