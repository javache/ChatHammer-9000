package ch9k.plugins.carousel;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.chat.events.RequestPluginPanelEvent;
import ch9k.chat.events.RequestedPluginPanelEvent;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventFilter;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.flickr.FlickrImageProviderPlugin;
import java.awt.Container;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin implements EventListener {
    /**
     * The main view for this plugin.
     */
    private CarouselPanel panel;

    /**
     * Selection model for this plugin.
     */
    public CarouselImageModel model;

    @Override
    public void enablePlugin(Conversation conversation) {
        super.enablePlugin(conversation);

        /* First, register this plugin as listener so it can receive a panel. */
        EventFilter filter = new EventFilter(RequestedPluginPanelEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        /* Asyncrhonously request a panel for this plugin. */
        Event event = new RequestPluginPanelEvent(conversation);
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public void disablePlugin() {
        super.disablePlugin();
        EventPool.getAppPool().removeListener(this);
        panel.disablePlugin();
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is not relevant. */
        RequestedPluginPanelEvent event = (RequestedPluginPanelEvent) e;
        if(!isRelevant(event)) return;

        /* Okay, we have a panel now, start using it. */
        JPanel container = event.getPluginPanel();
        model = new CarouselImageModel();
        panel = new CarouselPanel(getConversation(), model);
        container.add(panel);
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        Contact contact = new Contact("JPanneel", InetAddress.getByName("google.be"), false);
        Conversation conversation = new Conversation(contact, true);
        CarouselPlugin plugin = new CarouselPlugin();
        plugin.enablePlugin(conversation);
        //plugin.onReceivePanel(frame.getContentPane());
        frame.pack();
        frame.setTitle("Carousel test.");
        frame.setVisible(true);

        FlickrImageProviderPlugin flickr = new FlickrImageProviderPlugin();
        flickr.enablePlugin(conversation);
        flickr.sendNewImageEvent("lolcat");
        flickr.sendNewImageEvent("face");
        flickr.sendNewImageEvent("fire");
        flickr.sendNewImageEvent("abc");
        flickr.sendNewImageEvent("test");
        flickr.sendNewImageEvent("rain");
    }
}
