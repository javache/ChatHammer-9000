package be.jaspervdj.awesome;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.Plugin;
import javax.swing.JPanel;

/**
 * Awesome plugin.
 */
public class AwesomePlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings) {
        return new Awesome(plugin, conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return null;
    }

    @Override
    protected Settings createDefaultSettings() {
        return new Settings();
    }
}
