package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin {
    @Override
    public AbstractPluginInstance createPluginInstance(
            Conversation conversation) {
        return new Carousel(conversation);
    }
}
