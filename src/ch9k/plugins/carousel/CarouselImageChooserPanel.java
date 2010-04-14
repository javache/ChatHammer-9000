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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JButton;
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
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The image buttons.
     */
    private JButton[] imageButtons;

    /**
     * The relevant actionlisteners.
     */
    private ActionListener[] imageButtonActionListeners;

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

        Dimension labelSize = new Dimension(100, 100);
        imageButtons = new JButton[COLUMNS * 6];
        imageButtonActionListeners = new ActionListener[imageButtons.length];
        for(int i = 0; i < imageButtons.length; i++ ) {
            imageButtons[i] = new JButton();
            imageButtons[i].setPreferredSize(labelSize);
            imageButtons[i].setHorizontalAlignment(JButton.CENTER);
            imageButtons[i].setBorder(null);
            add(imageButtons[i]);
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

        /* Obtain the actual image and the size of the label. */
        Image image = event.getProvidedImage().getImage();
        Dimension dimension = imageButtons[nextImageIndex].getSize();

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

        /* Scale the image and set the icon. */
        Image scaled = image.getScaledInstance((int) width,
                (int) height, Image.SCALE_SMOOTH);
        imageButtons[nextImageIndex].setIcon(new ImageIcon(scaled));

        /* Set a new actionlistener. and remove the old one. */
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.setProvidedImage(event.getProvidedImage());
            }
        };
        if(imageButtonActionListeners[nextImageIndex] != null) {
            imageButtons[nextImageIndex].removeActionListener(
                    imageButtonActionListeners[nextImageIndex]);
        }
        imageButtonActionListeners[nextImageIndex] = listener;
        imageButtons[nextImageIndex].addActionListener(listener);

        /* Increment the image index. */
        nextImageIndex = (nextImageIndex + 1) % imageButtons.length;
    }
}
