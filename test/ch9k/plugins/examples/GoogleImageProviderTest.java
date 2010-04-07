package ch9k.plugins.examples;

import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jasper Van der Jeugt
 */
public class GoogleImageProviderTest {    
    /**
     * Test of getImageUrls method, of class GoogleImageProvider.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        GoogleImageProvider instance = new GoogleImageProvider();
        String[] results = instance.getImageUrls("lolcat", 10);

        /* Check that we don't get too much results back. */
        assertTrue(results.length <= 10);

        /* Check that we get back URL's. */
        for(String result: results) {
            /* This will throw a MalformedURLException if the URL is malformed.
             * JUnit will then inform us that the test has failed. */
            URL url = new URL(result);
        }
    }
}
