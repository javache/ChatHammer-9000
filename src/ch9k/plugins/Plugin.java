package ch9k.plugins;

import ch9k.chat.Conversation;

/**
 * Abstract representation of a plugin.
 * @author Jasper Van der Jeugt
 */
public interface Plugin {
    /**
     * Start this plugin by binding it to a conversation.
     * @param conversation Conversation to enable this plugin for.
     */
    void enablePlugin(Conversation conversation);

    /**
     * Stop this plugin by unbinding it from it's conversation.
     */
    void disablePlugin();
}
