package be.jaspervdj.googleimage;

import ch9k.core.settings.Settings;
import ch9k.plugins.ImageProviderTester;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoogleImageProviderTest {    
    /**
     * Test of getImageUrls method, of class GoogleImageProvider.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTester test = new ImageProviderTester();
        test.testGetImageUrls(new GoogleImageProvider(null, new Settings()));
    }
}
