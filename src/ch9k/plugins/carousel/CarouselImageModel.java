package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.core.Model;
import ch9k.plugins.ProvidedImage;

/**
 * A model class describing the image selection.
 */
public class CarouselImageModel extends Model {
    /**
     * Current image.
     */
    private ProvidedImage image; 

    /**
     * The conversation.
     */
    private Conversation conversation;

    /**
     * Constructor.
     */
    public CarouselImageModel(Conversation conversation)
    {
        image = null;
        this.conversation = conversation;
    }

    /**
     * Set a provided image.
     * @param image New image to set as selection.
     */
    public void setProvidedImage(ProvidedImage image) {
        if(this.image == null && image != null ||
                this.image != null && !this.image.equals(image)) {
            this.image = image;
            fireStateChanged();
        }
    }

    /**
     * Get the provided image.
     * @return The provided image.
     */
    public ProvidedImage getProvidedImage() {
        return image;
    }

    /**
     * Get the relevant conversation.
     * @return The relevant conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }
}
