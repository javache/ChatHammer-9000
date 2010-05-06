package ch9k.plugins.event;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEvent;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;

/**
 * Event thrown when a user enables or disables a plugin.
 */
public class RemotePluginSettingsChangeEvent extends ConversationEvent {
    /**
     * The plugin name.
     */
    private String plugin;

    /**
     * The settings change event.
     */
    private SettingsChangeEvent changeEvent;

    /**
     * Constructor.
     * @param conversation Conversation to which the plugin belongs.
     * @param name The plugin name.
     * @param changeEvent The local change event.
     */
    public RemotePluginSettingsChangeEvent(Conversation conversation,
            String plugin, SettingsChangeEvent changeEvent) {
        super(conversation);
        this.changeEvent = changeEvent;
    }

    /**
     * Obtain the plugin name.
     * @return The plugin name.
     */
    public String getPlugin() {
        return plugin;
    }

    /**
     * Obtain the local change event.
     * @return The local change event.
     */
    public SettingsChangeEvent getChangeEvent() {
        return changeEvent;
    }
}


