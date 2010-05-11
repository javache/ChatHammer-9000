package ch9k.plugins.event;

import java.net.URL;
import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEvent;

/**
 * Event thrown when an ImageProvider finds a new relevant image.
 */
public class NewImageURLEvent extends ConversationEvent {
    /**
     * URL of the image.
     */
    private URL url;

    /**
     * Constructor.
     * @param conversation Conversation to which the image belongs.
     * @param url URL of the image.
     */
    public NewImageURLEvent(
            Conversation conversation, URL url) {
        super(conversation);
        this.url = url;
    }

    /**
     * Obtain the image URL.
     * @return The image URL.
     */
    public URL getURL() {
        return url;
    }
}
