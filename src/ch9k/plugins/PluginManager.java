package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.core.Model;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.event.PluginChangeEvent;
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
public class PluginManager extends Model implements EventListener {
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

        /* Some plugins are always available, because they ship with the
         * application. */
        availablePlugins.add("ch9k.plugins.carousel.CarouselPlugin");
        availablePlugins.add("ch9k.plugins.flickr.FlickrImageProviderPlugin");
        availablePlugins.add(
                "ch9k.plugins.liteanalyzer.LiteTextAnalyzerPlugin");


        /* Register as listener. We will listen to remote enable/disable plugin
         * events, so we can synchronize with the plugin manager on the other
         * side. */
        EventFilter filter = new EventFilter(PluginChangeEvent.class);
        EventPool.getAppPool().addListener(this, filter);
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
        fireStateChanged();
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
        if(enable(conversation, name)) {
            /* Throw an event. */
            Event event = new PluginChangeEvent(conversation, name, true);
            EventPool.getAppPool().raiseEvent(event);
        }
    }

    /**
     * Enable a plugin for a given conversation.
     * @param conversation Conversation to enable the plugin for.
     * @param name Name of the plugin to load.
     * @return If the operation was succesful.
     */
    private boolean enable(Conversation conversation, String name) {
        /* Check that the plugin is not already enabled for the conversation. */
        Set<String> conversationPlugins = enabledPlugins.get(conversation);
        if(conversationPlugins != null && conversationPlugins.contains(name)) {
            return false;
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
                return false;
            }
        }

        /* Initialize the plugin. */
        Plugin plugin = null;
        try {
            plugin = (Plugin) pluginClass.newInstance();
        } catch (InstantiationException exception) {
            logger.warn("Could not instantiate " + name + ": " + exception);
            return false;
        } catch (IllegalAccessException exception) {
            logger.warn("Could not access " + name + ": " + exception);
            return false;
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

        return true;
    }

    /**
     * Disable a plugin for a given conversation.
     * @param conversation Conversation to disable the plugin for.
     * @param name Name of the plugin to disable.
     */
    public void disablePlugin(Conversation conversation, String name) {
        if(disable(conversation, name)) {
            /* Throw an event. */
            Event event = new PluginChangeEvent(conversation, name, false);
            EventPool.getAppPool().raiseEvent(event);
        }
    }

    /**
     * Disable a plugin for a given conversation.
     * @param conversation Conversation to disable the plugin for.
     * @param name Name of the plugin to disable.
     * @return If the action was succesful.
     */
    private boolean disable(Conversation conversation, String name) {
        Set<String> names = enabledPlugins.get(conversation);
        List<Plugin> instances = plugins.get(conversation);
        boolean succes = false;

        if(names != null && names.contains(name)) {
            names.remove(name);
            for(Plugin plugin: instances) {
                if(plugin.getClass().getName().equals(name)) {
                    plugin.disablePlugin();
                    instances.remove(plugin);
                    succes = true;
                }
            }
        }

        return succes;
    }

    /**
     * Access the plugin installer.
     * @return The plugin installer.
     */
    public PluginInstaller getPluginInstaller() {
        return installer;
    }

    @Override
    public void handleEvent(Event e) {
        PluginChangeEvent event = (PluginChangeEvent) e;
        /* A plugin was enabled. */
        if(event.isPluginEnabled()) {
            /* If the event was external, enable the plugin here as well. */
            if(event.isExternal()) {
                enable(event.getConversation(), event.getPlugin());
            }
        /* A plugin was disabled. */
        } else {
            /* If the event was external, disable the plugin here as well. */
            if(event.isExternal()) {
                disable(event.getConversation(), event.getPlugin());
            }
        }
    }
}
