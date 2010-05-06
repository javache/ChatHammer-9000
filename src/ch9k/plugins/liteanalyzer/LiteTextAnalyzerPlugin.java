package ch9k.plugins.liteanalyzer;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.Plugin;
import ch9k.plugins.AbstractPluginInstance;
import javax.swing.JPanel;

/**
 * A lite text analyzer plugin. The PRO version is available for onle 29.99
 * euros!
 */
public class LiteTextAnalyzerPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings) {
        return new LiteTextAnalyzer(plugin, conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return new LiteTextAnalyzerPreferencePane(settings);
    }

    @Override
    protected Settings createDefaultSettings() {
        Settings settings = new Settings();
        settings.setInt(LiteTextAnalyzerPreferencePane.MAX_SUBJECTS, 3);
        settings.setInt(LiteTextAnalyzerPreferencePane.MAX_MESSAGES, 5);
        return settings;
    }
}
