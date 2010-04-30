package ch9k.plugins.events;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import ch9k.plugins.Plugin;

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
     * Constructor.
     * @param conversation Conversation to which the plugin belongs.
     * @param name The plugin name.
     * @param pluginEnabled If the plugin was enabled.
     */
    public PluginChangeEvent(Conversation conversation,
            String plugin, boolean pluginEnabled) {
        super(conversation);
        this.plugin = plugin;
        this.pluginEnabled = pluginEnabled;
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
}

