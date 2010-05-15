package ch9k.plugins.event;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEvent;
import java.net.URL;

/**
 * Event thrown when a user recommends an image.
 */
public class RecommendedImageURLEvent extends ConversationEvent {
    /**
     * The image url.
     */
    private URL url;

    /**
     * Constructor.
     * @param conversation Conversation to which the image belongs.
     * @param url URL of the recommended image.
     */
    public RecommendedImageURLEvent(Conversation conversation, URL url) {
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
