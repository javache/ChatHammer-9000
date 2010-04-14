package ch9k.plugins.carousel;

import ch9k.chat.Contact;
import ch9k.chat.Conversation;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.flickr.FlickrImageProviderPlugin;
import java.awt.Container;
import java.net.InetAddress;
import javax.swing.JFrame;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin {
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

        // TODO: Request panel from conversation, add CarouselPanel there.
    }

    @Override
    public void disablePlugin() {
        super.disablePlugin();
        panel.disablePlugin();
    }

    public void onReceivePanel(Container container) {
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
        plugin.onReceivePanel(frame.getContentPane());
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
