package ch9k.plugins.carousel;

import ch9k.eventpool.WarningEvent;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

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
     * Create a new image. This will block for a while, as it loads the image
     * from the URL.
     * @param url URL for the image.
     */
    public ProvidedImage(URL url) {
        try {
            this.url = url;
            image = ImageIO.read(this.url);
        } catch (IOException exception) {
            WarningEvent.raise(this,
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

    /**
     * Get a pretty name.
     * @return A pretty name for the image to show in a GUI.
     */
    public String getFileName() {
        if(url == null) return null;
        return new File(url.getFile()).getName();
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
