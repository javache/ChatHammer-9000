package be.jaspervdj.googleimage;

import ch9k.chat.Conversation;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;

/**
 * Plugin handling google images.
 */
public class GoogleImageProviderPlugin extends AbstractPlugin {
    @Override
    public AbstractPluginInstance createPluginInstance(
            Conversation conversation) {
        return new GoogleImageProvider(conversation);
    }
}
