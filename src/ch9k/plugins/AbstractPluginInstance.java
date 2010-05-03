package ch9k.plugins;

import ch9k.chat.Conversation;

/**
 * Class that can serve as a base class for a simple plugin instance.
 * @author Jasper Van der Jeugt
 */
public abstract class AbstractPluginInstance {
    /**
     * The conversation we are bound to.
     */
    private Conversation conversation;

    /**
     * Constructor.
     * @param conversation Conversation to bind the plugin to.
     */
    public AbstractPluginInstance(Conversation conversation) {
        this.conversation = conversation;
    }

    /**
     * Get the conversation this plugin is coupled with.
     * @return The coupled conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Called when the plugin instance is started.
     */
    public abstract void enablePluginInstance();

    /**
     * Called when the plugin instance is disabled.
     */
    public abstract void disablePluginInstance();
}
