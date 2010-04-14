package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.NewProvidedImageEvent;

/**
 * Plugin for a standard image carousel.
 */
public class CarouselPlugin extends AbstractPlugin {
    /**
     * The main view for this plugin.
     */
    private CarouselPanel panel;

    @Override
    public void enablePlugin(Conversation conversation) {
        super.enablePlugin(conversation);

        // TODO: Request panel from conversation, add CarouselPanel there.
        panel = new CarouselPanel(conversation);
    }

    @Override
    public void disablePlugin() {
        super.disablePlugin();
        panel.disablePlugin();
    }
}
