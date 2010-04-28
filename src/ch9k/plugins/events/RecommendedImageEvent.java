package ch9k.plugins.events;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import ch9k.plugins.ProvidedImage;

/**
 * Event thrown when a user recommends an image.
 */
public class RecommendedImageEvent extends ConversationEvent {
    /**
     * The actual image.
     */
    private ProvidedImage image;

    /**
     * Constructor.
     * @param conversation Conversation to which the image belongs.
     * @param image The actual image.
     */
    public RecommendedImageEvent(Conversation conversation,
            ProvidedImage image) {
        super(conversation);
        this.image = image;
    }

    /**
     * Obtain the actual image.
     * @return The image.
     */
    public ProvidedImage getProvidedImage() {
        return image;
    }
}
