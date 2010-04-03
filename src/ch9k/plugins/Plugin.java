package ch9k.plugins;

import ch9k.chat.Conversation;

/**
 * Abstract representation of a plugin.
 * @author Jasper Van der Jeugt
 */
public interface Plugin {
    /**
     * Enable this plugin for a specific conversation.
     * @param conversation Conversation to enable this plugin for.
     */
    void enable(Conversation conversation);

    /**
     * Disable this plugin for a specific conversation.
     * @param conversation Conversation to disable this plugin for.
     */
    void disable(Conversation conversation);
}
