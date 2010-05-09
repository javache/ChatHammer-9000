package ch9k.plugins;

import ch9k.configuration.Storage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;
import static org.junit.Assert.*;

public class PluginManagerTest {    
    /**
     * Test plugin installation.
     */
    @Test
    public void testInstallPlugin() throws MalformedURLException {
        /* Remove the original plugin. */
        File file = new File(Storage.getStorageDirectory(),
                "plugins/DummyPlugin.jar");
        file.delete();

        /* Obtain the plugin manager. */
        PluginManager manager = new PluginManager();
        PluginInstaller installer = manager.getPluginInstaller();

        /* Install the plugin from the web. */
        String url = "http://zeus.ugent.be/~jasper/DummyPlugin.jar";
        installer.installPlugin(new URL(url));

        /* The plugin class should now be available. */
        String expectedPlugin = "DummyPlugin";
        assertTrue(manager.getPrettyNames().keySet().contains(expectedPlugin));
        String fullName = manager.getPrettyNames().get(expectedPlugin);
        assertEquals("be.ugent.zeus.DummyPlugin", fullName);

        /* Now, we will remove the plugin. We should then no longer have it
         * available. */
        manager.softRemovePlugin(fullName);
        assertFalse(manager.getPrettyNames().keySet().contains(expectedPlugin));
    }
}
