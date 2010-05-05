package ch9k.plugins.carousel;

import ch9k.chat.event.ConversationEventFilter;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.ProvidedImage;
import ch9k.plugins.event.NewProvidedImageEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Panel in which the user can select an image.
 */
public class CarouselImageChooserPanel extends JPanel implements ChangeListener {
    /**
     * Number of images visible. MUST BE ODD.
     */
    private static final int NUM_IMAGES = 5;

    /**
     * Spacing between images (in pixels).
     */
    private static final int SPACING = 10;

    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The chooser model.
     */
    private CarouselImageChooserModel chooserModel;

    /**
     * Constructor.
     * @param model The selection model of the plugin.
     */
    public CarouselImageChooserPanel(CarouselImageModel model) {
        super();
        this.model = model;
        this.chooserModel = new CarouselImageChooserModel(model); 

        setBackground(new Color(50, 50, 50));

        chooserModel.addChangeListener(this);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        chooserModel.disablePlugin();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        /* Do not forget to draw two extra images. */
        int index = (int) chooserModel.getCurrentSelection();
        double offset = (double) index - chooserModel.getCurrentSelection();
        for(int i = -1; i <= NUM_IMAGES; i++) {
            Image image = chooserModel.getImage(index + i - NUM_IMAGES / 2);
            drawImage(graphics, image, i, offset);
        }
    }

    private void drawImage(Graphics graphics, Image image, int index,
            double offset) {
        if(image == null) {
            return;
        }

        Insets insets = getInsets();
        double width = (double) getWidth() - insets.left - insets.right;
        double height = (double) getHeight() - insets.top - insets.bottom;

        double imageWidth = (width - NUM_IMAGES * 2 * SPACING) / NUM_IMAGES;
        double x = offset + imageWidth * (double) index + imageWidth * 0.5;
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        repaint();
    }
}
