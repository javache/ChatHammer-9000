package ch9k.plugins.examples;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.Photo;
import java.util.logging.Logger;

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
     * Logger, well, to log.
     */
    private final static Logger LOGGER =
            Logger.getLogger("ch9k.plugins.examples.FlickrImageProvider");

    /**
     * Constructor.
     */
    FlickrImageProvider() {
        flickr = new Flickr(API_KEY);
    }

    /**
     * Search for some text on Flickr.
     * @param text Text to search for.
     * @return List of URL's.
     */
    public String[] searchFlickr(String text) {
        return searchFlickr(text, 10);
    }

    /**
     * Search for some text on Flickr.
     * @param text Text to search for.
     * @param maxResults Maximum results to return.
     * @return List of URL's.
     */
    public String[] searchFlickr(String text, int maxResults) {
        /* We first need to initialize our photos interface, and construct
         * search parameters based on the text. */
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setText(text);
        LOGGER.info("Searching for: " + text);

        /* Now search for the text. We limit ourselves to maxResuls photos. */
        PhotoList photoList;
        try {
            photoList = photosInterface.search(searchParameters, maxResults, 0);
        } catch (Exception exception) {
            // TODO: Do something proper.
            return null;
        }

        /* Put the results in a list of URL's, and log it as well. */
        String[] urls = new String[photoList.size()];
        for(int i = 0; i < urls.length; i++) {
            Photo photo = (Photo) photoList.get(i);
            urls[i] = photo.getMediumUrl();
            LOGGER.info("Photo result: " + photo.getTitle() + " - " +
                    photo.getMediumUrl());
        }

        return urls;
    }

    /**
     * Main function for testing purposes. Searches for "lolcat" on Flickr.
     */
    public static void main(String[] args) {
        FlickrImageProvider f = new FlickrImageProvider();
        f.searchFlickr("lolcat");
    }
}
