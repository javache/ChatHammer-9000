package ch9k.plugins;

import java.net.MalformedURLException;
import java.net.URL;
import static org.junit.Assert.*;

public class ImageProviderTester {
    /**
     * Test of getImageUrls method, of any ImageProvider.
     */
    public void testGetImageUrls(ImageProvider instance)
            throws MalformedURLException {
        /* Search for a sentence. */
        String[] results = instance.getImageUrls("lolcat", 10);

        /* Check that we don't get to much results back. */
        assertTrue(results.length <= 10);

        /* Check that we get back URL's. */
        for(String result: results) {
            /* This will throw a MalformedURLException if the URL is malformed.
             * JUnit will then inform us that the test has failed. */
            URL url = new URL(result);

            /* We support a number of image formats, so check that we're dealing
             * with an image. */
            String string = result.toLowerCase();
            assertTrue(string.endsWith(".jpg") || string.endsWith(".png") ||
                    string.endsWith(".gif") || string.endsWith(".jpeg"));
        }
    }
}
