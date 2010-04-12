package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import java.awt.Image;

public class NewImageEvent extends ConversationEvent {
    /**
     * The actual image.
     */
    private Image image;

    /**
     * Constructor.
     * @param conversation Conversation to which the image belongs.
     * @param image The actual image.
     */
    public NewImageEvent(Conversation conversation, Image image) {
        super(conversation);
        this.image = image;
    }

    /**
     * Obtain the actual image.
     * @return The image.
     */
    public Image getImage() {
        return image;
    }
}
