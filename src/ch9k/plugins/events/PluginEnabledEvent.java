package ch9k.plugins.events;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import ch9k.plugins.Plugin;

/**
 * Event thrown when a user enables a plugin.
 */
public class PluginEnabledEvent extends ConversationEvent {
    /**
     * The plugin name.
     */
    private String plugin;

    /**
     * Constructor.
     * @param conversation Conversation to which the plugin belongs.
     * @param name The plugin name.
     */
    public PluginEnabledEvent(Conversation conversation, String plugin) {
        super(conversation);
        this.plugin = plugin;
    }

    /**
     * Obtain the plugin name.
     * @return The plugin name.
     */
    public String getPlugin() {
        return plugin;
    }
}

