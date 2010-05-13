package be.jaspervdj.wordcloud;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.Plugin;
import javax.swing.JPanel;

/**
 * WordCloud plugin.
 */
public class WordCloudPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Plugin plugin, Conversation conversation, Settings settings) {
        return new WordCloud(plugin, conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return new WordCloudPreferencePane(settings);
    }

    @Override
    protected Settings createDefaultSettings() {
        Settings settings = new Settings();
        settings.setInt(WordCloudPreferencePane.MAX_WORDS, 40);
        return settings;
    }
}
