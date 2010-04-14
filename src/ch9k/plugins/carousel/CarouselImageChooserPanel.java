package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.NewProvidedImageEvent;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * Panel in which the user can select an image.
 */
public class CarouselImageChooserPanel extends JPanel implements EventListener {
    /**
     * The conversation.
     */
    private Conversation conversation;

    /**
     * Constructor.
     * @param conversation The conversation.
     */
    public CarouselImageChooserPanel(Conversation conversation) {
        super(new GridLayout(0, 6));
        this.conversation = conversation;

        EventFilter filter = new EventFilter(NewProvidedImageEvent.class);
        EventPool.getAppPool().addListener(this, filter);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        EventPool.getAppPool().removeListener(this);
    }

    @Override
    public void handleEvent(Event e) {
        /* Return if the event is not relevant. */
        NewProvidedImageEvent event = (NewProvidedImageEvent) e;
        if(conversation != event.getConversation()) return;

        // TODO
    }
}
