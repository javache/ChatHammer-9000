package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;

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
     * @param setting Settings for the plugin.
     */
    void enablePlugin(Conversation conversation, Settings settings);

    /**
     * Stop a plugin for a conversation.
     */
    void disablePlugin(Conversation conversation);

    /**
     * Soft remove (disable) a plugin.
     */
    public void softRemove();

    /**
     * Get a pretty name for the plugin.
     * @return A human-readable name.
     */
    String getPrettyName();

    /**
     * Get the settings for this plugin.
     */
    Settings getSettings();
}
