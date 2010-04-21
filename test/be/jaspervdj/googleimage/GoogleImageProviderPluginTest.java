package be.jaspervdj.googleimage;

import ch9k.plugins.ImageProviderTester;
import java.net.MalformedURLException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoogleImageProviderPluginTest {    
    /**
     * Test of getImageUrls method, of class GoogleImageProviderPlugin.
     */
    @Test
    public void testGetImageUrls() throws MalformedURLException {
        ImageProviderTester test = new ImageProviderTester();
        test.testGetImageUrls(new GoogleImageProviderPlugin());
    }
}
