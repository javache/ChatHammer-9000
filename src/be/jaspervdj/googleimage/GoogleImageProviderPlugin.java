package be.jaspervdj.googleimage;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.Plugin;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.ImageProviderPreferencePane;
import javax.swing.JPanel;

/**
 * Plugin handling google images.
 */
public class GoogleImageProviderPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings) {
        return new GoogleImageProvider(plugin, conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return new ImageProviderPreferencePane(settings);
    }

    @Override
    protected Settings createDefaultSettings() {
        return new Settings();
    }
}
