package be.jaspervdj.awesome;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.ReleasePluginContainerEvent;
import ch9k.chat.event.RequestPluginContainerEvent;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.core.settings.Settings;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.Plugin;
import ch9k.plugins.AbstractPluginInstance;
import java.awt.Container;
import javax.swing.JLabel;

/**
 * Awesome plugin view.
 */
public class Awesome extends AbstractPluginInstance implements EventListener {
    /**
     * The container that we get from the system.
     */
    private Container container;

    /**
     * Constructor.
     * @param plugin Corresponding plugin.
     * @param conversation Conversation to display carousel for.
     * @param settings Local plugin instance settings.
     */
    public Awesome(Plugin plugin,
            Conversation conversation, Settings settings) {
        super(plugin, conversation, settings);
        /* We will asynchronously receive a container later. */
        this.container = null;
    }

    @Override
    public void enablePluginInstance() {
        /* First, register this plugin as listener so it can receive a container
         * later. */
        EventFilter filter = new ConversationEventFilter(
                RequestedPluginContainerEvent.class, getConversation());
        EventPool.getAppPool().addListener(this, filter);

        /* Asyncrhonously request a panel for this plugin. */
        Event event = new RequestPluginContainerEvent(getConversation(),
                "AWESOME");
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public void disablePluginInstance() {
        /* Disable the plugin. */
        EventPool.getAppPool().removeListener(this);

        /* Release the container request a panel for this plugin. */
        Event event =
                new ReleasePluginContainerEvent(getConversation(), container);
        EventPool.getAppPool().raiseEvent(event);
        container.removeAll();
        container = null;
    }

    @Override
    public void handleEvent(Event e) {
        RequestedPluginContainerEvent event = (RequestedPluginContainerEvent) e;

        /* We only need one panel. */
        if(container != null) return;

        /* Okay, we have a panel now, start using it. */
        container = event.getPluginContainer();

        for(int i = 0; i < 50; i++) {
            container.add(new JLabel("AWESOME"));
        }

        container.validate();
        container.repaint();
    }
}
