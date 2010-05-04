package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;

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
     * The current settings.
     */
    private Settings settings;

    /**
     * Constructor.
     * @param conversation Conversation to bind the plugin to.
     * @param settings Local plugin settings.
     */
    public AbstractPluginInstance(
            Conversation conversation, Settings settings) {
        this.conversation = conversation;
        this.settings = settings;
    }

    /**
     * Get the conversation this plugin is coupled with.
     * @return The coupled conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }

    /**
     * Get the current local settings.
     * @return The local settings for this instance.
     */
    public Settings getSettings() {
        return settings;
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
