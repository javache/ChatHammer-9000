package ch9k.plugins;

import ch9k.chat.Conversation;
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
     * Constructor.
     */
    public AbstractPlugin() {
        instances = new HashMap<Conversation, AbstractPluginInstance>();
    }

    /**
     * Obtain the name of this plugin.
     * @return The name of this plugin.
     */
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isEnabled(Conversation conversation) {
        return instances.get(conversation) != null;
    }

    @Override
    public void enablePlugin(Conversation conversation) {
        /* Create a new instance, store it, and enable it. */
        AbstractPluginInstance instance = createPluginInstance(conversation);
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

    /**
     * Abstract factory method creating the actual plugin.
     */
    protected abstract AbstractPluginInstance
            createPluginInstance(Conversation conversation);
}
