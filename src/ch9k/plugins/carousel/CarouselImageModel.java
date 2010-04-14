package ch9k.plugins.carousel;

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
     * Constructor.
     */
    public CarouselImageModel()
    {
        image = null;
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
}
