package ch9k.plugins.flickr;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.ImageProviderPreferencePane;
import ch9k.plugins.Plugin;
import javax.swing.JPanel;

/**
 * Plugin that gives us the ability to look up images on Flickr.
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProviderPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings) {
        return new FlickrImageProvider(plugin, conversation, settings);
    }

    @Override
    public JPanel createPreferencePane(Settings settings) {
        return new ImageProviderPreferencePane(settings);
    }

    @Override
    public Settings createDefaultSettings() {
        return new Settings();
    }
}
