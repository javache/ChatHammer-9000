package ch9k.plugins;

import ch9k.configuration.Storage;
import ch9k.eventpool.WarningEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;

/**
 * A class that simplifies the process of installing new plugins.
 */
public class PluginInstaller extends URLClassLoader {
    /**
     * Logger logger logger
     * Mushroom Mushroom
     */
    private static final Logger logger =
            Logger.getLogger(PluginInstaller.class);

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
     * @param pluginManager
     */
    public PluginInstaller(PluginManager pluginManager) {
        /* The superclass is an URLClassLoader with no predefined paths. */
        super(new URL[0]);

        /* Store a reference to the plugin manager. */
        this.pluginManager = pluginManager;
    }

    /**
     * Load locally installed plugins.
     */
    public void loadInstalledPlugins() {
        /* Create the install directory if it doesn't exist yet. */
        if(!INSTALL_DIRECTORY.isDirectory()) {
            INSTALL_DIRECTORY.mkdirs();
        }

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
     * @return Name of the registred plugin.
     */
    public String registerPlugin(File file) {
        logger.info("Registering plugin: " + file);
        Manifest manifest = null;

        try {
            JarFile jar = new JarFile(file);
            manifest = jar.getManifest();
        } catch (IOException exception) {
            WarningEvent.raise(this,
                "Could not register plugin " + file + ": " + exception);
            logger.warn(exception.toString());
        }

        /* Retreat, retreat! */
        if(manifest == null) {
            WarningEvent.raise(this,
                "No jar manifest found in " + file);
            return null;
        }

        /* Obtain the jar manifest and it's attributes. */
        Attributes attributes = manifest.getMainAttributes();

        /* Add the jar file to the class path. */
        try {
            addURL(file.toURI().toURL());
        } catch (MalformedURLException exception) {
            /* This will never happen, since we know that file is valid now. */
            return null;
        }

        /* Find the plugin name .*/
        String pluginName = attributes.getValue("Plugin-Class");

        /* No plugin in this jar. This doesn't matter, it could be a dependency
         * containing no plugin's itself. */
        if(pluginName == null) {
            return null;
        }

        /* Register the plugin class. */
        logger.info("Plugin found: " + pluginName);
        pluginManager.addAvailablePlugin(pluginName, file.getAbsolutePath());

        return pluginName;
    }

    /**
     * Load a plugin class.
     * @param name Name of the class to load.
     * @return The plugin class.
     * @throws ClassNotFoundException
     */
    public Class<?> getPluginClass(String name) throws ClassNotFoundException {
        return findClass(name);
    }

    /**
     * Install a plugin from an URL.
     * @param url URL pointing to a plugin jar.
     * @return Name of the installed plugin.
     */
    public synchronized String installPlugin(URL url) {
        logger.info("Installing plugin: " + url);
        try {
            /* We take the filename of the url and store the plugin there. */
            URLConnection connection = url.openConnection();
            File file = new File(url.getFile());
            return installPlugin(connection.getInputStream(), file.getName());
        } catch (IOException exception) {
            WarningEvent.raise(this,
                "Could not get plugin " + url + ": " + exception);
            return null;
        }
    }

    /**
     * Install a plugin jar from an input stream. This will close the stream
     * after the read.
     * @param in Stream to get the plugin from.
     * @param fileName File name of the plugin.
     * @throws IOException
     * @return Name of the installed plugin.
     */
    public synchronized String installPlugin(InputStream in, String fileName)
            throws IOException {
        /* Open the output file. */
        File file = new File(INSTALL_DIRECTORY, fileName);
        OutputStream out = new FileOutputStream(file);

        /* Buffer and buffer length. */
        byte[] buffer = new byte[1024];
        int length;

        /* Copy the stream. */
        while((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        /* Close the streams. */
        in.close();
        out.close();

        /* Register the new plugin. */
        return registerPlugin(file);
    }
}
