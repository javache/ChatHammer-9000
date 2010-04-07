package ch9k.plugins.examples;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProviderTest {    
    /**
     * Test of searchFlickr method, of class FlickrImageProvider.
     */
    @Test
    public void testSearchFlickr() throws MalformedURLException {
        FlickrImageProvider instance = new FlickrImageProvider();
        String[] results = instance.searchFlickr("lolcat", 10);

        /* Check that we get enough results back. */
        assertEquals(results.length, 10);

        /* Check that we get back URL's. */
        for(String result: results) {
            /* This will throw a MalformedURLException if the URL is malformed.
             * JUnit will then inform us that the test has failed. */
            URL url = new URL(result);

            /* Flickr supports a number of image formats, so check that 
             * we're dealing with an image. */
            assertTrue(result.endsWith(".jpg") || result.endsWith(".png") ||
                    result.endsWith(".gif"));
        }
    }
}
