package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;

/**
 * Event thrown when an ImageProvider finds a new relevant image.
 */
public class NewProvidedImageEvent extends ConversationEvent {
    /**
     * The actual image.
     */
    private ProvidedImage image;

    /**
     * Constructor.
     * @param conversation Conversation to which the image belongs.
     * @param image The actual image.
     */
    public NewProvidedImageEvent(
            Conversation conversation, ProvidedImage image) {
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
