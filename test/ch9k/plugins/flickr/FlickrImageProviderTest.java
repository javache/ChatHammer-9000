package ch9k.plugins.flickr;

import ch9k.core.settings.Settings;
import ch9k.plugins.ImageProviderTester;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlickrImageProviderTest {    
    /**
     * Test of getImageUrls method, of class FlickrImageProvider.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTester tester = new ImageProviderTester();
        tester.testGetImageUrls(new FlickrImageProvider(null, new Settings()));
    }
}
