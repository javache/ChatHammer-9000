package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.core.settings.Settings;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;
import javax.swing.JPanel;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin {
    @Override
    protected AbstractPluginInstance createPluginInstance(
            Conversation conversation, Settings settings) {
        return new Carousel(conversation, settings);
    }

    @Override
    protected JPanel createPreferencePane(Settings settings) {
        return null;
    }
}
