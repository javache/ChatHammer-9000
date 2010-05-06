package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import javax.swing.JPanel;

import javax.swing.JFrame;
import java.net.InetAddress;
import ch9k.chat.Contact;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.plugins.flickr.FlickrImageProvider;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Conversation conversation, Settings settings) {
        return new Carousel(conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return null;
    }

    @Override
    protected Settings createDefaultSettings() {
        return new Settings();
    }

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        Contact contact = new Contact("JPanneel",
                InetAddress.getByName("google.be"));
        Conversation conversation = new Conversation(contact, true);
        CarouselPlugin plugin = new CarouselPlugin();
        plugin.enablePlugin(conversation, new Settings());

        //plugin.onReceivePanel(frame.getContentPane());
        Event event = new RequestedPluginContainerEvent(conversation,
                frame.getContentPane());
        EventPool.getAppPool().raiseEvent(event);

        frame.pack();
        frame.setTitle("Carousel test.");
        frame.setVisible(true);

        FlickrImageProvider flickr = new FlickrImageProvider(conversation,
                new Settings());
        flickr.sendNewImageEvent("jailbait");
    }
}
