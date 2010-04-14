package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import java.awt.Dimension;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.NewProvidedImageEvent;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

/**
 * Panel in which the user can select an image.
 */
public class CarouselImageChooserPanel extends JPanel implements EventListener {
    /**
     * The conversation.
     */
    private Conversation conversation;

    /**
     * The image labels.
     */
    private JLabel[] imageLabels;

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
     */
    public CarouselImageChooserPanel(Conversation conversation) {
        super(new GridLayout(0, COLUMNS));
        this.conversation = conversation;

        Dimension labelSize = new Dimension(100, 100);
        imageLabels = new JLabel[COLUMNS * 6];
        for(int i = 0; i < imageLabels.length; i++ ) {
            imageLabels[i] = new JLabel("Image " + i);
            imageLabels[i].setPreferredSize(labelSize);
            System.out.println("Added image.");
            add(imageLabels[i]);
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
        NewProvidedImageEvent event = (NewProvidedImageEvent) e;
        if(conversation != event.getConversation()) return;

        /* Obtain the actual image and the size of the label. */
        Image image = event.getProvidedImage().getImage();
        Dimension dimension = imageLabels[nextImageIndex].getSize();

        /* Find out the ascpet ratio of the image. */
        double imageAspect =
                (double) image.getWidth(null) / image.getHeight(null);

        /* Scale by width. */
        double width = dimension.getWidth();
        double height = (double) width / imageAspect;

        /* We're wrong, scale by height. */
        if(height < dimension.getHeight()) {
            height = dimension.getHeight();
            width = imageAspect * (double) height;
        }

        Image scaled = image.getScaledInstance((int) width,
                (int) height, Image.SCALE_SMOOTH);

        imageLabels[nextImageIndex].setIcon(new ImageIcon(scaled));
        nextImageIndex = (nextImageIndex + 1) % imageLabels.length;
    }
}
