package ch9k.plugins.carousel;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.RequestPluginContainerEvent;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.core.settings.Settings;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.flickr.FlickrImageProviderPlugin;
import java.awt.GridLayout;
import java.awt.Container;
import java.net.InetAddress;
import javax.swing.JFrame;

/**
 * Plugin for a standard image carousel.
 */
public class Carousel extends AbstractPluginInstance implements EventListener {
    /**
     * The container that we get from the system.
     */
    private Container container;

    /**
     * The main view for this plugin.
     */
    private CarouselPanel panel;

    /**
     * Selection model for this plugin.
     */
    public CarouselImageModel model;

    /**
     * Constructor.
     * @param conversation Conversation to display carousel for.
     * @param settings Local plugin instance settings.
     */
    public Carousel(Conversation conversation, Settings settings) {
        super(conversation, settings);
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
        Event event = new RequestPluginContainerEvent(getConversation());
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public void disablePluginInstance() {
        /* Disable the plugin. */
        EventPool.getAppPool().removeListener(this);
        panel.disablePlugin();

        /* Remove everything from the container. */
        container.removeAll();
        container.validate();
        container.repaint();
        container = null;
    }

    @Override
    public void handleEvent(Event e) {
        RequestedPluginContainerEvent event = (RequestedPluginContainerEvent) e;

        /* We only need one panel. */
        if(container != null) return;

        /* Okay, we have a panel now, start using it. */
        container = event.getPluginContainer();

        /* Clear the container and set a new layout. */
        container.removeAll();
        container.setLayout(new GridLayout(1, 1));

        /* Add our carousel to it. */
        model = new CarouselImageModel(getConversation());
        panel = new CarouselPanel(model);
        container.add(panel);

        /* Redraw the container. */
        container.validate();
    }
}
