package ch9k.plugins.flickr;

import ch9k.eventpool.WarningMessageEvent;
import ch9k.plugins.ImageProvider;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * Class that gives us the ability to look up images on Flickr.
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProviderPlugin extends ImageProvider {
    /**
     * Flickr API key for ch9k.
     */
    private static final String API_KEY = "421079f2d0a110192bdd2e46dc603c77";

    /**
     * Flickr API handle.
     */
    private final Flickr flickr;

    /**
     * Logger, well, to log.
     */
    private static final Logger logger =
            Logger.getLogger(FlickrImageProviderPlugin.class);

    /**
     * Constructor.
     */
    public FlickrImageProviderPlugin() {
        flickr = new Flickr(API_KEY);
    }

    @Override
    public String[] getImageUrls(String text, int maxResults) {
        /* We first need to initialize our photos interface, and construct
         * search parameters based on the text. */
        PhotosInterface photosInterface = flickr.getPhotosInterface();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setText(text);
        logger.info("Searching for " + text);

        /* Now search for the text. We limit ourselves to maxResuls photos. */
        PhotoList photoList;
        try {
            photoList = photosInterface.search(searchParameters, maxResults, 0);
        } catch (IOException exception) {
            WarningMessageEvent.raiseWarningMessageEvent(this,
                "Could not search Flickr: " + exception);
            return null;
        } catch (SAXException exception) {
            WarningMessageEvent.raiseWarningMessageEvent(this,
                "Could not parse Flickr response: " + exception);
            return null;
        } catch (FlickrException exception) {
            WarningMessageEvent.raiseWarningMessageEvent(this,
                "Flickr internal error: " + exception);
            return null;
        }

        /* Put the results in a list of URL's, and log it as well. */
        String[] urls = new String[photoList.size()];
        for(int i = 0; i < urls.length; i++) {
            Photo photo = (Photo) photoList.get(i);
            urls[i] = photo.getMediumUrl();
            logger.info("Photo result: " + photo.getTitle() + " - " +
                    photo.getMediumUrl());
        }

        return urls;
    }
}
