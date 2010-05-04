package ch9k.plugins.liteanalyzer;

import ch9k.chat.Conversation;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;

/**
 * A lite text analyzer plugin. The PRO version is available for onle 29.99
 * euros!
 */
public class LiteTextAnalyzerPlugin extends AbstractPlugin {
    @Override
    public AbstractPluginInstance createPluginInstance(
            Conversation conversation) {
        return new LiteTextAnalyzer(conversation);
    }
}
