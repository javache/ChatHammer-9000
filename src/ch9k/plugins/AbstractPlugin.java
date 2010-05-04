package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import java.util.HashMap;
import java.util.Map;

/**
 * Class implementing Plugin for convenience reasons.
 * @author Jasper Van der Jeugt
 */
public abstract class AbstractPlugin implements Plugin {
    /**
     * Map of active plugins instances.
     */
    private transient Map<Conversation, AbstractPluginInstance> instances;

    /**
     * The plugin settings.
     */
    private Settings settings;

    /**
     * Constructor.
     */
    public AbstractPlugin() {
        instances = new HashMap<Conversation, AbstractPluginInstance>();
        settings = new Settings();
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
     * @param conversation Conversation to create the plugin instance for.
     * @param settings Settings to use for the instance.
     * @return A plugin instance.
     */
    protected abstract AbstractPluginInstance
            createPluginInstance(Conversation conversation, Settings settings);

    @Override
    public boolean isEnabled(Conversation conversation) {
        return instances.get(conversation) != null;
    }

    @Override
    public void enablePlugin(Conversation conversation, Settings settings) {
        /* Create a new instance, store it, and enable it. */
        AbstractPluginInstance instance =
                createPluginInstance(conversation, settings);
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
    public String getPrettyName() {
        String name = getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    @Override
    public Settings getSettings() {
        return settings;
    }
}
