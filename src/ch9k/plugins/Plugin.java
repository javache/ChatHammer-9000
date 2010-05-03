package ch9k.plugins;

import ch9k.chat.Conversation;

/**
 * Abstract representation of a plugin.
 * @author Jasper Van der Jeugt
 */
public interface Plugin {
    /**
     * Check if the plugin is enabled for a certain conversation.
     * @param conversation Conversation to check for.
     * @return If this plugin is enabled for the given conversation.
     */
    public boolean isEnabled(Conversation conversation);

    /**
     * Start a plugin for a conversation.
     * @param conversation Conversation to enable this plugin for.
     */
    void enablePlugin(Conversation conversation);

    /**
     * Stop a plugin for a conversation.
     */
    void disablePlugin(Conversation conversation);
}
