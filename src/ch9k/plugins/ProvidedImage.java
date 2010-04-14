package ch9k.plugins;

import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Image class for images provided by an ImageProvider.
 */
public class ProvidedImage {
    /**
     * URL the image was loaded from. Very suited as unique identifier.
     */
    private String url;

    /**
     * The image delegate.
     */
    private Image image;

    /**
     * Create a new image.
     * @param url URL for the image.
     * @param image The actual image.
     */
    public ProvidedImage(String url) {
        this.url = url;
        try {
            /* Create an image, and send it using an event. */
            ImageIcon tmp = new ImageIcon(new URL(url));
            image = tmp.getImage();
        } catch (MalformedURLException exception) {
            // TODO: Send warning event.
        }
    }

    /**
     * Get the URL this image was loaded from.
     * @return The URL this image was loaded from.
     */
    public String getURL() {
        return url;
    }

    /**
     * Get the actual image.
     * @return The actual image.
     */
    public Image getImage() {
        return image;
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof ProvidedImage) || object == null) return false;
        return url.equals(((ProvidedImage) object).getURL());
    }
}
