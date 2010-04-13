package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.chat.events.NewChatMessageEvent;
import ch9k.chat.events.NewConversationSubjectEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.eventpool.TypeEventFilter;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Class abstracting image providing.
 * @author Jasper Van der Jeugt
 */
public abstract class ImageProvider extends AbstractPlugin
        implements EventListener {
    @Override
    public void enable(Conversation conversation) {
        super.enable(conversation);
        EventFilter filter =
                new TypeEventFilter(NewConversationSubjectEvent.class);
        EventPool.getAppPool().addListener(this, filter);
    }

    @Override
    public void disable() {
        EventPool.getAppPool().removeListener(this);
    }

    /**
     * Send new images, searching for text.
     * @param text Text to search for on the image provider.
     */
    private void sendNewImageEvent(String text) {
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
        /* Return if the event is not relevant. */
        NewConversationSubjectEvent event = (NewConversationSubjectEvent) e;
        if(!isRelevant(event)) return;

        /* Construct a new text to search for by appending subjects. */
        ConversationSubject subject = event.getConversationSubject();
        String[] subjects = subject.getSubjects();
        String text = "";
        if(subjects.length > 0) text = subjects[0];
        for(int i = 1; i < subjects.length; i++) text += " " + subjects[i];

        /* Send the new image event. */
        sendNewImageEvent(text);
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
