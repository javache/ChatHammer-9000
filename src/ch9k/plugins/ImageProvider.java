package ch9k.plugins;

import java.net.URL;
import java.net.MalformedURLException;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Class abstracting image providing.
 * @author Jasper Van der Jeugt
 */
public abstract class ImageProvider {
    /**
     * Constructor.
     */
    public ImageProvider()
    {
    }

    /**
     * Name of method is subject to change.
     */
    public void onNewText(String text)
    {
        /* Get the URL's from which the images should be loaded. */
        String[] urls = getImageUrls(text);
        
        /* Load the actual images. */
        for(String url: urls) {
            try {
                ImageIcon tmp = new ImageIcon(new URL(url));
                Image image = tmp.getImage();
                // TODO: Send event containing image, then continue.
            } catch (MalformedURLException exception) {
                // TODO: Send warning event.
            }
        }
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @return List of URL's.
     */
    public String[] getImageUrls(String text)
    {
        return getImageUrls(text, 10);
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @param maxResults Maximum results to return.
     * @return List of URL's.
     */
    public abstract String[] getImageUrls(String text, int maxResults);
}
