package ch9k.plugins;

import ch9k.eventpool.WarningMessageEvent;
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
    private URL url;

    /**
     * The image delegate.
     */
    private Image image;

    /**
     * Create a new image.
     * @param url URL for the image.
     */
    public ProvidedImage(String url) {
        try {
            /* Create an image, and send it using an event. */
            this.url = new URL(url);
            ImageIcon tmp = new ImageIcon(this.url);
            image = tmp.getImage();
        } catch (MalformedURLException exception) {
            WarningMessageEvent.raiseWarningMessageEvent(this,
                "Could not get image " + url + ": " + exception);
        }
    }

    /**
     * Get the URL this image was loaded from.
     * @return The URL this image was loaded from.
     */
    public URL getURL() {
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
        if(!(object instanceof ProvidedImage) || object == null) {
            return false;
        }
        return url.equals(((ProvidedImage) object).getURL());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.url != null ? this.url.hashCode() : 0);
        return hash;
    }
}
