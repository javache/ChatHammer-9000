package ch9k.plugins.liteanalyzer;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.Plugin;
import ch9k.plugins.TextAnalyzerPreferencePane;
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
        return new TextAnalyzerPreferencePane(settings);
    }

    @Override
    protected Settings createDefaultSettings() {
        Settings settings = new Settings();
        settings.setInt(TextAnalyzerPreferencePane.MAX_SUBJECTS, 3);
        settings.setInt(TextAnalyzerPreferencePane.MAX_MESSAGES, 5);
        settings.setInt(TextAnalyzerPreferencePane.TRIGGER_INTERVAL, 20);
        return settings;
    }
}
