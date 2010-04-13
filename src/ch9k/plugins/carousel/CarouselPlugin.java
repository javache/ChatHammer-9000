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
public class CarouselPlugin extends AbstractPlugin implements EventListener {
    /**
     * The main view for this plugin.
     */
    private CarouselPanel panel;

    @Override
    public void enable(Conversation conversation) {
        super.enable(conversation);
        EventFilter filter = new EventFilter(NewProvidedImageEvent.class);
        EventPool.getAppPool().addListener(this, filter);

        // TODO: Request panel from conversation, add CarouselPanel there.
    }

    @Override
    public void disable() {
        EventPool.getAppPool().removeListener(this);
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is not relevant. */
        NewProvidedImageEvent event = (NewProvidedImageEvent) e;
        if(!isRelevant(event)) return;

        // TODO: Pass event to GUI.
    }
}
