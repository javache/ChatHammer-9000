package ch9k.plugins.event;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEvent;
import ch9k.core.settings.Settings;

/**
 * Event thrown when a user enables or disables a plugin.
 */
public class PluginChangeEvent extends ConversationEvent {
    /**
     * The plugin name.
     */
    private String plugin;

    /**
     * If the plugin was enabled.
     */
    private boolean pluginEnabled;

    /**
     * Reference to the plugin settings.
     */
    private Settings settings;

    /**
     * Constructor.
     * @param conversation Conversation to which the plugin belongs.
     * @param name The plugin name.
     * @param pluginEnabled If the plugin was enabled.
     * @param settings Settings of the plugin.
     */
    public PluginChangeEvent(Conversation conversation,
            String plugin, boolean pluginEnabled, Settings settings) {
        super(conversation);
        this.plugin = plugin;
        this.pluginEnabled = pluginEnabled;
        this.settings = settings;
    }

    /**
     * Obtain the plugin name.
     * @return The plugin name.
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * See if the plugin was enabled.
     * @return If the plugin was enabled.
     */
    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    /**
     * Get the plugin settings. This function is allowed to return null, so
     * use it with care.
     * @return The plugin settings.
     */
    public Settings getSettings() {
        return settings;
    }
}
