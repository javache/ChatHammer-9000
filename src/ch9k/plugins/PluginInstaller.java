package ch9k.plugins;

import java.net.MalformedURLException;
import java.util.jar.JarFile;
import java.util.Map;
import java.util.jar.Manifest;
import java.util.jar.Attributes;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.logging.Logger;
import ch9k.configuration.Storage;
import java.net.URLClassLoader;
import java.net.URL;

/**
 * A class that simplifies the process of installing new plugins.
 */
public class PluginInstaller extends URLClassLoader {
    /**
     * Loggger to get some more output.
     */
    private static final Logger LOGGER =
            Logger.getLogger(PluginInstaller.class.getName());

    /**
     * Hardcoded for now. Needs to be in Configuration somewhere.
     */
    private static final File INSTALL_DIRECTORY =
            new File(Storage.getStorageDirectory(), "plugins");

    /**
     * A reference to the plugin manager.
     */
    private PluginManager pluginManager;

    /**
     * Constructor.
     */
    public PluginInstaller(PluginManager pluginManager) {
        /* The superclass is an URLClassLoader with no predefined paths. */
        super(new URL[0]);

        /* Store a reference to the plugin manager. */
        this.pluginManager = pluginManager;

        /* Create the install directory if it doesn't exist yet. */
        if(!INSTALL_DIRECTORY.isDirectory())
            INSTALL_DIRECTORY.mkdirs();

        /* Create a file filter to search for jar's. */
        FileFilter jarFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.getName().endsWith(".jar");
            }
        };

        /* Get all jars in the plugin directory. */
        File[] files = INSTALL_DIRECTORY.listFiles(jarFilter);

        /* Add every jar to the classpath. */
        for(File file: files) {
            registerPlugin(file);
        }
    }

    /**
     * Register a plugin to the configuration and plugin manager.
     * This file should be located in the application directory already,
     * use installPlugin otherwise.
     * @param file File to register as plugin.
     */
    public void registerPlugin(File file) {
        LOGGER.info("Registering plugin: " + file);
        Manifest manifest = null;

        try {
            JarFile jar = new JarFile(file);
            manifest = jar.getManifest();
        } catch (IOException exception) {
            // TODO: Show relevant warning.
        }

        /* Retreat, retreat! */
        if(manifest == null) return;

        /* Obtain the jar manifest and it's attributes. */
        Attributes attributes = manifest.getMainAttributes();

        /* Add the jar file to the class path. */
        try {
            addURL(file.toURI().toURL());
        } catch (MalformedURLException exception) {
            // TODO: Show relevant warning.
            return;
        }

        /* Find the plugin name .*/
        String pluginName = attributes.getValue("Plugin-Name");

        /* No plugin in this jar. */
        if(pluginName == null) return;

        /* Register the plugin class. */
        LOGGER.info("Plugin found: " + pluginName);
        pluginManager.addAvailablePlugin(pluginName);
    }

    /**
     * Load a plugin class.
     * @param name Name of the class to load.
     * @return The plugin class.
     */
    public Class getPluginClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }
}
