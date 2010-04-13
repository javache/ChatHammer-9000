package be.jaspervdj.googleimage;

import ch9k.plugins.ImageProviderTest;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Jasper Van der Jeugt
 */
public class GoogleImageProviderPluginTest {    
    /**
     * Test of getImageUrls method, of class GoogleImageProviderPlugin.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTest test = new ImageProviderTest();
        test.testGetImageUrls(new GoogleImageProviderPlugin());
    }
}
