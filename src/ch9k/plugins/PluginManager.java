package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.event.CloseConversationEvent;
import ch9k.chat.event.ConversationWindowReadyEvent;
import ch9k.configuration.Persistable;
import ch9k.configuration.PersistentDataObject;
import ch9k.core.ChatApplication;
import ch9k.core.I18n;
import ch9k.core.Model;
import ch9k.core.settings.Settings;
import ch9k.core.settings.event.PreferencePaneEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.NetworkEvent;
import ch9k.plugins.event.PluginChangeEvent;
import ch9k.plugins.event.RequestPluginEvent;
import ch9k.plugins.event.RequestedPluginEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * A singleton to manage plugins.
 * @author Jasper Van der Jeugt
 */
public class PluginManager extends Model implements EventListener, Persistable {
    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(PluginManager.class);

    /**
     * We keep the plugins by name.
     */
    private Map<String, Plugin> plugins;

    /**
     * We keep the filename for every plugin as well.
     */
    private Map<String, String> fileNames;

    /**
     * For every plugin, we also keep the desirable state.
     */
    private final Map<String, Map<Conversation, Boolean>> pluginState;

    /**
     * A plugin installer.
     */
    private PluginInstaller installer;

    /**
     * Constructor.
     */
    public PluginManager() {
        plugins = new HashMap<String, Plugin>();
        fileNames = new HashMap<String, String>();
        pluginState = new HashMap<String, Map<Conversation, Boolean>>();
        installer = new PluginInstaller(this);

        /* Register as listener. We will listen to remote enable/disable plugin
         * events, so we can synchronize with the plugin manager on the other
         * side. */
        EventFilter filter = new EventFilter(PluginChangeEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* We want to listen to closing conversations. If the user closes a
         * conversation, we want to disable all it's plugins. */
        filter = new EventFilter(CloseConversationEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* We want to listen to request plugin events */
        filter = new EventFilter(RequestPluginEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* We surely want to listen to these events,
         * so we can install plugins */
        filter = new EventFilter(RequestedPluginEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* When a conversation window is ready, we enable the default plugins
         * for that conversation. */
        filter = new EventFilter(ConversationWindowReadyEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* Throw our preference pane so the user can install more plugins */
        JPanel preferencePane = new PluginPreferencePane(this);
        Event event = new PreferencePaneEvent(
                I18n.get("ch9k.plugins", "plugins") , preferencePane);
        EventPool.getAppPool().raiseEvent(event);
    }

    /**
     * Constructor.
     * @param pdo PersistentDataObject to load settings from.
     */
    public PluginManager(PersistentDataObject pdo) {
        this();
        load(pdo);
    }

    /**
     * Load any plugins currently installed
     */
    public void loadInstalledPlugins() {
        /* Load plugins lazily */
        new Thread(new Runnable() {
            public void run() {
                installer.loadInstalledPlugins();

                /* Some plugins are always available, because they ship with the
                 * application. */
                addAvailablePlugin("ch9k.plugins.carousel.CarouselPlugin", null);
                addAvailablePlugin(
                        "ch9k.plugins.flickr.FlickrImageProviderPlugin", null);
                addAvailablePlugin(
                        "ch9k.plugins.liteanalyzer.LiteTextAnalyzerPlugin", null);
            }
        }, "PluginManager-initializer").start();
    }

    /**
     * Get a map of pretty names for the plugins. This map binds pretty names
     * to actual plugin names.
     * @return A map of pretty names for the plugins.
     */
    public Map<String, String> getPrettyNames() {
        Map<String, String> map = new HashMap<String, String>();
        for(String plugin: plugins.keySet()) {
            map.put(plugins.get(plugin).getPrettyName(), plugin);
        }

        return map;
    }

    /**
     * Get a list of default plugins. These "default" plugins are the ones that
     * ship with the main application and cannot be removed.
     * @return A list of default plugins.
     */
    public List<String> getDefaultPlugins() {
        List<String> defaultPlugins = new ArrayList<String>();
        for(String name: plugins.keySet()) {
            if(getPluginFileName(name) == null) {
                defaultPlugins.add(name);
            }
        }

        return defaultPlugins;
    }

    /**
     * Add an available plugin to the list.
     * @param name Class name of the plugin to add.
     * @param fileName Filename of the plugin.
     */
    public synchronized void addAvailablePlugin(String name, String fileName) {
        /* Check that we don't have it already. */
        if(plugins.get(name) != null) {
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
        /* This error will happen when we still have this in memory */
        } catch (LinkageError e) {
            try {
                pluginClass = Class.forName(name);
            } catch(ClassNotFoundException ex) {
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

        plugins.put(name, plugin);
        fileNames.put(name, fileName);
        fireStateChanged();
    }

    /**
     * Soft remove a plugin.
     * @param name Name of the plugin to soft-remove.
     */
    public void softRemovePlugin(String name) {
        /* Obtain the plugin. */
        Plugin plugin = plugins.get(name);
        if(plugin == null) {
            return;
        }

        /* Disable the plugin for every conversation. */
        for(Conversation conversation:
                ChatApplication.getInstance().getConversationManager()) {
            disablePlugin(name, conversation);
        }

        /* Remove the plugin from the lists. */
        plugins.remove(name);

        /* Actually remove the file. */
        String fileName = fileNames.get(name);
        if(fileName != null) {
            File file = new File(fileName);
            file.delete();
        }

        plugin.softRemove();

        fireStateChanged();
    }

    /**
     * Check if a given plugin is enabled for a given conversation.
     * @param name Name of the plugin to check.
     * @param conversation Conversation to check.
     * @return If the given plugin is enabled for the given conversation.
     */
    public boolean isEnabled(String name, Conversation conversation) {
        Plugin plugin = plugins.get(name);
        if(plugin == null) {
            return false;
        } else {
            return plugin.isEnabled(conversation);
        }
    }

    /**
     * Enable a plugin for a given conversation.
     * @param name Name of the plugin to load.
     * @param conversation Conversation to enable the plugin for.
     */
    public void enablePlugin(String name, Conversation conversation) {
        enablePlugin(name, conversation, true);
    }

    /**
     * Enable a plugin for a given conversation.
     * @param name Name of the plugin to load.
     * @param conversation Conversation to enable the plugin for.
     * @param networkEvent If we should send a network event for this change.
     */
    protected void enablePlugin(String name,
            Conversation conversation, boolean networkEvent) {
        /* We are enabling this plugin, so we use our settings. */
        Plugin plugin = plugins.get(name);
        Settings settings = null;
        if(plugin != null) {
            settings = plugin.getSettings();
        }

        if(enable(name, conversation, settings)) {
            /* Throw an event. */
            PluginChangeEvent event =
                    new PluginChangeEvent(conversation, name, true, settings);

            if(networkEvent) {
                EventPool.getAppPool().raiseNetworkEvent(event);
            } else {
                EventPool.getAppPool().raiseEvent(event);
            }
        }
    }

    /**
     * Enable a plugin for a given conversation.
     * @param name Name of the plugin to load.
     * @param conversation Conversation to enable the plugin for.
     * @param settings Settings for the plugin.
     * @return If the operation was succesful.
     */
    private synchronized boolean enable(
            String name, Conversation conversation, Settings settings) {
        /* Retrieve the plugin. */
        Plugin plugin = plugins.get(name);
        setPluginState(name, conversation, true);

        /* Check that we have the plugin installed. */
        if(plugin == null) {
            int result = JOptionPane.showConfirmDialog(null,
                    I18n.get("ch9k.plugins", "plugin_not_installed", name),
                    I18n.get("ch9k.plugins", "request_plugin"),
                    JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION) {
                /* Send request event. */
                RequestPluginEvent event =
                        new RequestPluginEvent(conversation, name);
                EventPool.getAppPool().raiseNetworkEvent(event);
            }

            return false;
        }

        /* Check that the plugin is not already enabled for the conversation. */
        if(plugin.isEnabled(conversation)) {
            return false;
        }

        /* Couple it with the conversation. */
        plugin.enablePlugin(conversation, settings);

        return true;
    }

    /**
     * Disable a plugin for a given conversation.
     * @param name Name of the plugin to disable.
     * @param conversation Conversation to disable the plugin for.
     */
    public synchronized void disablePlugin(
            String name, Conversation conversation) {
        disablePlugin(name, conversation, true);
    }

    /**
     * Disable a plugin for a given conversation.
     * @param name Name of the plugin to disable.
     * @param conversation Conversation to disable the plugin for.
     * @param networkEvent If we should send a network event for this change.
     */
    protected synchronized void disablePlugin(
            String name, Conversation conversation, boolean networkEvent) {
        if(disable(name, conversation)) {
            /* Throw an event. */
            PluginChangeEvent event =
                    new PluginChangeEvent(conversation, name, false, null);

            if(networkEvent) {
                EventPool.getAppPool().raiseNetworkEvent(event);
            } else {
                EventPool.getAppPool().raiseEvent(event);
            }
        }
    }

    /**
     * Disable a plugin for a given conversation.
     * @param name Name of the plugin to disable.
     * @param conversation Conversation to disable the plugin for.
     * @return If the action was succesful.
     */
    private synchronized boolean disable(
            String name, Conversation conversation) {
        Plugin plugin = plugins.get(name);
        setPluginState(name, conversation, false);
        if(plugin == null || !plugin.isEnabled(conversation)) {
            return false;
        }

        plugin.disablePlugin(conversation);

        return true;
    }

    /**
     * Set the desirable state for a plugin.
     * @param name Plugin name.
     * @param conversation Conversation to change the state for.
     * @param enabled State of the plugin.
     */
    private void setPluginState(String name,
            Conversation conversation, boolean enabled) {
        Map<Conversation, Boolean> states = pluginState.get(name);
        if(states == null) {
            states = new HashMap<Conversation, Boolean>();
            pluginState.put(name, states);
        }
        states.put(conversation, enabled);
    }

    /**
     * Get the desirable state for a plugin.
     * @param name Plugin name.
     * @param conversation Conversation to change the state for.
     * @return State of the plugin.
     */
    private boolean getPluginState(String name, Conversation conversation) {
        Map<Conversation, Boolean> states = pluginState.get(name);
        if(states == null) {
            return false;
        } else {
            Boolean result = states.get(conversation);
            if(result == null) {
                return false;
            } else {
                return result.booleanValue();
            }
        }
    }

    /**
     * Get the file name for a given plugin. This function will return null if
     * the plugin was not loaded from a file.
     * @param name Name of the plugin to find the file for.
     * @return The file name of the plugin file.
     */
    public String getPluginFileName(String name) {
        return fileNames.get(name);
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
        /* A PluginChangeEvent. */
        if(e instanceof PluginChangeEvent) {
            PluginChangeEvent event = (PluginChangeEvent) e;
            /* A plugin was enabled. */
            if(event.isPluginEnabled()) {
                /* If the event was external, enable the plugin here as well. */
                if(event.isExternal()) {
                    enable(event.getPlugin(), event.getConversation(),
                            event.getSettings());
                }
            /* A plugin was disabled. */
            } else {
                /* If the event was external, disable the plugin here as
                 * well. */
                if(event.isExternal()) {
                    disable(event.getPlugin(), event.getConversation());
                }
            }
        }

        /* A CloseConversationEvent. */
        if(e instanceof CloseConversationEvent) {
            CloseConversationEvent event = (CloseConversationEvent) e;

            /* Disable every possible plugin for the conversation. */
            for(String name: plugins.keySet()) {
                disablePlugin(name, event.getConversation());
            }
        }

        /* A RequestPluginEvent */
        if(e instanceof RequestPluginEvent) {
            final RequestPluginEvent event = (RequestPluginEvent) e;
            
            /* Do not react if this is an internal event. */
            if(!event.isExternal()) return;

            new Thread(new Runnable() {
                public void run() {
                    try {
                        /* See if we have this plugin available. */
                        String name = event.getPlugin();
                        String fullFillName = fileNames.get(name);
                        if(fullFillName == null) return;

                        /* Convert it to a byte array */
                        File file = new File(fullFillName);
                        String fileName = file.getName();

                        /* Get the size of the file. */
                        InputStream input = new FileInputStream(file);
                        int length = (int) file.length();

                        /* Create the byte array to hold the data. */
                        byte[] bytes = new byte[length];

                        /* Read in the bytes. */
                        int offset = 0;
                        int numRead = 0;
                        while (offset < bytes.length &&
                                (numRead = input.read(bytes,
                                        offset, bytes.length-offset)) >= 0) {
                            offset += numRead;
                        }

                        /* Send the event. */
                        NetworkEvent answer = new RequestedPluginEvent(
                                (InetAddress) event.getSource(), bytes,
                                fileName);
                        EventPool.getAppPool().raiseNetworkEvent(answer);
                    } catch (IOException exception) {
                        /* Throw warning. */
                    }
                }
            }).start();
        }

        /* A RequestedPluginEvent */
        if(e instanceof RequestedPluginEvent) {
            final RequestedPluginEvent event = (RequestedPluginEvent)e;

            /* Do not react is it is internal. */
            if(event.isExternal()) {
                new Thread(new Runnable() {
                    public void run() {
                        /* Convert the data to an InputStream */
                        InputStream in =    
                                new ByteArrayInputStream(event.getData());
                        String name = null;
                        try {
                            name = installer.installPlugin(
                                    in, event.getFilename());
                        } catch(IOException ex) {
                            logger.warn("Could not install plugin");
                        }

                        /* Search if we should enable this plugin. */
                        Map<Conversation, Boolean> states =
                                pluginState.get(name);
                        if(states == null) return;
                        for(Conversation conversation: states.keySet()) {
                            if(states.get(conversation) != null &&
                                    states.get(conversation).booleanValue()) {
                                enablePlugin(name, conversation);
                            }
                        }
                    }
                }).start();
            }
        }

        /* A ConversationWindowReadyEvent. */
        if(e instanceof ConversationWindowReadyEvent) {
            /* Obtain the conversation from the event. */
            Conversation conversation =
                    ((ConversationWindowReadyEvent) e).getConversation();
            
            /* Enable the default plugins for the conversation. */
            for(String name: getDefaultPlugins()) {
                enablePlugin(name, conversation, false);
            }
        }
    }

    @Override
    public PersistentDataObject persist() {
        Element pluginManager = new Element("pluginManager");
        for(String name: plugins.keySet()) {
            Plugin plugin = plugins.get(name);
            /* Persist only the settings. */
            Settings settings = plugin.getSettings();
            if(settings != null) {
                PersistentDataObject pdo = settings.persist();
                /* Get the element. */
                Element element = pdo.getElement();
                /* Set the correct name. */
                element.setAttribute("plugin", name);
                /* Add it as a child. */
                pluginManager.addContent(element);
            }
        }

        return new PersistentDataObject(pluginManager);
    }

    @Override
    public void load(PersistentDataObject pdo) {
        for(Object object : pdo.getElement().getChildren()) {
            Element child = (Element) object;

            /* Find the name of the plugin and get the corresponding plugin. */
            String name = child.getAttributeValue("plugin");
            Plugin plugin = plugins.get(name);

            if(plugin != null) {
                /* load the plugin settings from the XML. */
                Settings settings = plugin.getSettings();
                settings.load(new PersistentDataObject(child));
            }
        }
    }
}
