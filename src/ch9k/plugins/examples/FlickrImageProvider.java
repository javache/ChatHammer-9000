package ch9k.plugins.examples;

import com.aetrion.flickr.Flickr;

/**
 * Class that gives us the ability to look up images on Flickr.
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProvider {
    /**
     * Flickr API key for ch9k.
     */
    private final static String API_KEY = "421079f2d0a110192bdd2e46dc603c77";

    /**
     * Flickr API handle.
     */
    private final Flickr flickr;

    /**
     * Constructor.
     */
    FlickrImageProvider() {
        flickr = new Flickr(API_KEY);
    }
}
