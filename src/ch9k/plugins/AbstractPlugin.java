package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;
import ch9k.core.settings.SettingsChangeListener;
import ch9k.core.settings.SettingsChangeListener;
import ch9k.core.settings.event.PreferencePaneEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.plugins.event.RemotePluginSettingsChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 * Class implementing Plugin for convenience reasons.
 * @author Jasper Van der Jeugt
 */
public abstract class AbstractPlugin
        implements Plugin, SettingsChangeListener {
    /**
     * Map of active plugins instances.
     */
    private transient Map<Conversation, AbstractPluginInstance> instances;

    /**
     * The plugin settings.
     */
    private Settings settings;

    /**
     * The preference pane.
     */
    private transient JPanel preferencePane;

    /**
     * Constructor.
     */
    public AbstractPlugin() {
        instances = new HashMap<Conversation, AbstractPluginInstance>();
        settings = createDefaultSettings();

        /* Throw our preference pane if the plugin needs one. */
        preferencePane = createPreferencePane(getSettings());
        if(preferencePane != null) {
            Event event = new PreferencePaneEvent(
                    getPrettyName(), preferencePane);
            EventPool.getAppPool().raiseEvent(event);
        }

        /* We want to be notified of new local events. */
        settings.addSettingsListener(this);
    }

    /**
     * Obtain the name of this plugin.
     * @return The name of this plugin.
     */
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * Abstract factory method creating the actual plugin.
     * @param plugin Reference to the plugin.
     * @param conversation Conversation to create the plugin instance for.
     * @param settings Settings to use for the instance.
     * @return A plugin instance.
     */
    protected abstract AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings);

    /**
     * Create a preference pane. Can return null -- in this case, the plugin
     * will have no preference pane.
     * @param settings Settings to use in the preference pane.
     * @return The preference pane.
     */
    protected abstract JPanel createPreferencePane(Settings settings);

    /**
     * Get the default settings.
     * @return The default settings for this plugin.
     */
    protected abstract Settings createDefaultSettings();

    @Override
    public boolean isEnabled(Conversation conversation) {
        return instances.get(conversation) != null;
    }

    @Override
    public void enablePlugin(Conversation conversation, Settings settings) {
        /* Create a new instance, store it, and enable it. */
        AbstractPluginInstance instance =
                createPluginInstance(this, conversation, settings);
        instances.put(conversation, instance);
        instance.enablePluginInstance();
    }

    @Override
    public void disablePlugin(Conversation conversation) {
        /* Remove the instance and disable it. */
        AbstractPluginInstance instance = instances.get(conversation);
        instances.remove(conversation);
        instance.disablePluginInstance();
    }

    @Override
    public void softRemove() {
        /* Remove the preference pane. */
        Event event = new PreferencePaneEvent(
                getPrettyName(), preferencePane, false);
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public String getPrettyName() {
        String name = getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public void settingsChanged(SettingsChangeEvent changeEvent) {
        /* The local settings changed. This change needs to be propagated to
         * all remote clients. */
        for(Conversation conversation: instances.keySet()) {
            /* Only propagate the settings if we started the conversation. */
            if(conversation.isInitiatedByMe()) {
                NetworkEvent event = new RemotePluginSettingsChangeEvent(
                        conversation, this.getClass().getName(), changeEvent);
                EventPool.getAppPool().raiseNetworkEvent(event);
            }
        }
    }
}
