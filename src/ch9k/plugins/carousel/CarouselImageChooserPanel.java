package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.NewProvidedImageEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
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
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The image items.
     */
    private CarouselImageChooserItem[] images;

    /**
     * Columns in the grid.
     */
    private final static int COLUMNS = 6;

    /**
     * Index of where to add the next image.
     */
    private int nextImageIndex;

    /**
     * Constructor.
     * @param conversation The conversation.
     * @param model The selection model of the plugin.
     */
    public CarouselImageChooserPanel(Conversation conversation,
            CarouselImageModel model) {
        super(new GridLayout(0, COLUMNS));
        this.conversation = conversation;
        this.model = model;

        setBackground(Color.BLACK);

        Dimension labelSize = new Dimension(100, 100);
        images = new CarouselImageChooserItem[COLUMNS * 6];
        for(int i = 0; i < images.length; i++ ) {
            images[i] = new CarouselImageChooserItem();
            images[i].setPreferredSize(labelSize);
            add(images[i]);
        }

        nextImageIndex = 0;

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
        // TODO: invokeLater

        /* Return if the event is not relevant. */
        final NewProvidedImageEvent event = (NewProvidedImageEvent) e;
        if(conversation != event.getConversation()) return;

        /* Set the new image. */
        images[nextImageIndex].setProvidedImage(event.getProvidedImage());

        /* Increment the image index. */
        nextImageIndex = (nextImageIndex + 1) % images.length;
    }

    private class CarouselImageChooserItem extends ImagePanel {
        public CarouselImageChooserItem() {
            super(false);
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            model.setProvidedImage(getProvidedImage());
        }
    }
}
