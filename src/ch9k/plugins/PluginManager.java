package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.plugins.events.PluginDisabledEvent;
import ch9k.plugins.events.PluginEnabledEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * A singleton to manage plugins.
 * @author Jasper Van der Jeugt
 */
public class PluginManager {
    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(PluginManager.class);

    /**
     * We keep, for every conversation, a list of activated plugins by name.
     */
    private Map<Conversation, Set<String>> enabledPlugins;

    /**
     * In addition, we keep for every conversation the list of actual plugin
     * instances.
     */
    private Map<Conversation, List<Plugin>> plugins;

    /**
     * A list of available plugins.
     */
    private List<String> availablePlugins;

    /**
     * A plugin installer.
     */
    private PluginInstaller installer;

    /**
     * Constructor.
     */
    public PluginManager() {
        enabledPlugins = new HashMap<Conversation, Set<String>>();
        plugins = new HashMap<Conversation, List<Plugin>>();
        availablePlugins = new ArrayList<String>();
        installer = new PluginInstaller(this);
    }

    /**
     * Get a list of available plugins.
     * @return A list of available plugins.
     */
    public List<String> getAvailablePlugins() {
        return availablePlugins;
    }

    /**
     * Add an available plugin to the list.
     * @param name Class name of the plugin to add.
     */
    public void addAvailablePlugin(String name) {
        availablePlugins.add(name);
    }

    /**
     * Get the list of enabled plugins for a conversation.
     * @param conversation Conversation to get a plugin list for.
     * @return The list of enabled plugins.
     */
    public Set<String> getEnabledPlugins(Conversation conversation) {
        return enabledPlugins.get(conversation);
    }

    /**
     * Enable a plugin for a given conversation.
     * @param conversation Conversation to enable the plugin for.
     * @param name Name of the plugin to load.
     */
    public void enablePlugin(Conversation conversation, String name) {
        /* Check that the plugin is not already enabled for the conversation. */
        Set<String> conversationPlugins = enabledPlugins.get(conversation);
        if(conversationPlugins != null && conversationPlugins.contains(name)) {
            return;
        }

        /* Find the class of the new plugin. */
        Class<?> pluginClass = null;
        try {
            pluginClass = installer.getPluginClass(name);
        } catch (ClassNotFoundException exception) {
            /* Try all stuff in the classpath now. */
            try {
                pluginClass = Class.forName(name);
            } catch (ClassNotFoundException e) {
                /* Should not happen, because we registered it earlier. */
                logger.warn("Class not found: " + name);
                return;
            }
        }

        /* Initialize the plugin. */
        Plugin plugin = null;
        try {
            plugin = (Plugin) pluginClass.newInstance();
        } catch (InstantiationException exception) {
            logger.warn("Could not instantiate " + name + ": " + exception);
            return;
        } catch (IllegalAccessException exception) {
            logger.warn("Could not access " + name + ": " + exception);
            return;
        }

        /* Couple it with the conversation. */
        plugin.enablePlugin(conversation);

        /* Register plugin. */
        if(conversationPlugins == null) {
            conversationPlugins = new HashSet<String>();
            List<Plugin> tmp = new ArrayList<Plugin>();
            plugins.put(conversation, tmp);
        }
        conversationPlugins.add(name);
        plugins.get(conversation).add(plugin); 

        /* Throw an event. */
        Event event = new PluginEnabledEvent(conversation, name);
        EventPool.getAppPool().raiseEvent(event);
    }

    /**
     * Disable a plugin for a given conversation.
     * @param conversation Conversation to disable the plugin for.
     * @param name Name of the plugin to disable.
     */
    public void disablePlugin(Conversation conversation, String name) {
        Set<String> names = enabledPlugins.get(conversation);
        List<Plugin> instances = plugins.get(conversation);

        if(names != null && names.contains(name)) {
            names.remove(name);
            for(Plugin plugin: instances) {
                if(plugin.getClass().getName().equals(name)) {
                    plugin.disablePlugin();
                    instances.remove(plugin);
                }
            }
        }

        /* Throw an event. */
        Event event = new PluginDisabledEvent(conversation, name);
        EventPool.getAppPool().raiseEvent(event);
    }

    /**
     * Access the plugin installer.
     * @return The plugin installer.
     */
    public PluginInstaller getPluginInstaller() {
        return installer;
    }
}
