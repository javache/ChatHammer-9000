package ch9k.plugins.carousel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JPanel;

/**
 * View showing a single image.
 */
public class CarouselImagePanel extends JPanel implements ChangeListener {
    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     * @param model The selection model of the plugin.
     */
    public CarouselImagePanel(CarouselImageModel model) {
        super();
        setPreferredSize(new Dimension(400, 200));
        this.model = model;
        model.addChangeListener(this);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        int width = getWidth();
        int height = getHeight();

        /* Paint background black. */
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

        /* If we have no image, we've now done enough. */
        if(model.getProvidedImage() == null ||
                model.getProvidedImage().getImage() == null) {
            return;
        }
        Image image = model.getProvidedImage().getImage();

        /* Find out the ascpet ratio of the image. */
        double imageAspect =
                (double) image.getWidth(null) / image.getHeight(null);

        /* Scale by width. */
        double imageWidth = width;
        double imageHeight = (double) imageWidth / imageAspect;

        /* We're wrong, scale by imageHeight. */
        if(imageHeight > height) {
            imageHeight = height;
            imageWidth = imageAspect * (double) imageHeight;
        }

        /* Actually draw the image. */
        graphics.drawImage(image, (int) (width * 0.5 - imageWidth * 0.5),
                (int) 0, (int) imageWidth, (int) imageHeight,
                Color.BLACK, null);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO: Be imageobserver
        /* Request a redraw. */
        repaint();
    }
}
