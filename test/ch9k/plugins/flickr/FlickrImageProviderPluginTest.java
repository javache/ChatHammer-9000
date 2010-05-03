package ch9k.plugins.flickr;

import ch9k.plugins.ImageProviderTester;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class FlickrImageProviderPluginTest {    
    /**
     * Test of getImageUrls method, of class FlickrImageProviderPlugin.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTester tester = new ImageProviderTester();
        tester.testGetImageUrls(new FlickrImageProviderPlugin(null));
    }
}
