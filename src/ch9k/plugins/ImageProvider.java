package ch9k.plugins;

import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Image;
import javax.swing.ImageIcon;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.chat.events.NewChatMessageEvent;

/**
 * Class abstracting image providing.
 * @author Jasper Van der Jeugt
 */
public abstract class ImageProvider extends AbstractPlugin
        implements EventListener {
    /**
     * Constructor.
     */
    public ImageProvider() {
        // TODO: Register as listener
    }

    /**
     * Name of method is subject to change.
     */
    public void sendNewImages(String text) {
        /* Get the URL's from which the images should be loaded. */
        String[] urls = getImageUrls(text);

        /* When the concrete implementation fails, it will return null as urls.
         * However, a relevant warning will be shown by the concrete
         * implementation, so we can just return here. */
        if (urls == null) return;
        
        /* Load the actual images. */
        for (String url: urls) {
            try {
                /* Create an image, and send it using an event. */
                ImageIcon tmp = new ImageIcon(new URL(url));
                Image image = tmp.getImage();
                NewImageEvent event =
                        new NewImageEvent(getConversation(), image);
                EventPool.getAppPool().raiseEvent(event);
            } catch (MalformedURLException exception) {
                // TODO: Send warning event.
            }
        }
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is no NewChatMessageEvent. */
        if(!(e instanceof NewChatMessageEvent)) return;
        NewChatMessageEvent event = (NewChatMessageEvent) e;

        /* Return if the event is not relevant. */
        if(!isRelevant(event)) return;

        /* For now, we just send new images on new text. */
        sendNewImages(event.getChatMessage().getText());
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @return List of URL's.
     */
    public String[] getImageUrls(String text) {
        return getImageUrls(text, 10);
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @param maxResults Maximum results to return.
     * @return List of URL's.
     */
    public abstract String[] getImageUrls(String text, int maxResults);
}
