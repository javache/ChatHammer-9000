package ch9k.plugins.flickr;

import ch9k.plugins.ImageProviderTest;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProviderPluginTest {    
    /**
     * Test of getImageUrls method, of class FlickrImageProviderPlugin.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTest test = new ImageProviderTest();
        test.testGetImageUrls(new FlickrImageProviderPlugin());
    }
}
