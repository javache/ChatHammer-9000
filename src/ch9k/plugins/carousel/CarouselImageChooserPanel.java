package ch9k.plugins.carousel;

import ch9k.core.settings.Settings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Panel in which the user can select an image.
 */
public class CarouselImageChooserPanel extends JPanel
        implements ChangeListener, MouseWheelListener, MouseListener {
    /**
     * Spacing between images (in pixels).
     */
    private static final double SPACING = 10.0;

    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The chooser model.
     */
    private CarouselImageChooserModel chooserModel;

    /**
     * Gradient for reflections.
     */
    private static Image gradient = null;
    
    static {
        try {
            gradient = ImageIO.read(
                    CarouselImageChooserPanel.class.getResource(
                            "/ch9k/plugins/carousel/gradient.png"));
        } catch (IOException exception) {
            // Ignore.
        }
    }

    /**
     * Constructor.
     * @param settings Settings of the plugin.
     * @param model The selection model of the plugin.
     */
    public CarouselImageChooserPanel(Settings settings,
            CarouselImageModel model) {
        super();
        this.model = model;
        this.chooserModel = new CarouselImageChooserModel(settings, model); 

        setBackground(new Color(50, 50, 50));
        setPreferredSize(new Dimension(0, 140));

        model.addChangeListener(this);
        chooserModel.addChangeListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        chooserModel.disablePlugin();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;

        /* Do not forget to draw two extra images. */
        int index = (int) chooserModel.getCurrentSelection();
        double offset = (double) index - chooserModel.getCurrentSelection();
        for(int i = -1; i <= chooserModel.getNumVisibleImages(); i++) {
            ProvidedImage image = chooserModel.getProvidedImage(
                    index + i - chooserModel.getNumVisibleImages() / 2);
            if(image != null) {
                drawImage(graphics, image.getImage(), i, offset,
                        image.equals(model.getProvidedImage()));
            }
        }
    }

    /**
     * Draw a single image.
     */
    private void drawImage(Graphics2D graphics, Image image, int index,
            double offset, boolean selectedInModel) {
        if(image == null) {
            return;
        }

        double width = (double) getWidth();
        double height = (double) getHeight();

        double imageMaxWidth = width /
                (double) chooserModel.getNumVisibleImages();
        double imageMaxHeight = height * 0.6;
        double x = imageMaxWidth * ((double) index + offset + 0.5);

        /* Find out the aspect ratio of the image. */
        double imageAspect =
                (double) image.getWidth(null) / image.getHeight(null);

        /* Scale by width. */
        double imageWidth = imageMaxWidth - 2 * SPACING;
        double imageHeight = imageWidth / imageAspect;

        /* We're wrong, scale by imageHeight. */
        if(imageHeight > imageMaxHeight) {
            imageHeight = imageMaxHeight;
            imageWidth = imageAspect * imageHeight;
        }

        /* Build a transformation for the image. */
        AffineTransform identity = new AffineTransform();
        AffineTransform original = graphics.getTransform();
        graphics.translate((int) x, height * 0.7);
        graphics.scale(imageWidth / (double) image.getWidth(null),
                imageHeight / (double) image.getHeight(null));
        graphics.translate(- (double) image.getWidth(null) * 0.5,
                -image.getHeight(null));
        graphics.drawImage(image, identity, null);

        /* Draw stroke. */
        if(selectedInModel) {
            graphics.setColor(Color.WHITE);
            graphics.drawRect(-2, -2, image.getWidth(null) + 2,
                    image.getHeight(null) + 2);
        }

        /* Scale and transform. */
        graphics.translate(0.0, image.getHeight(null) * 1.65);
        graphics.scale(1.0, - 0.6);
        graphics.drawImage(image, identity, null);

        /* Scale back to original format. */
        graphics.scale((double) image.getWidth(null) / imageWidth,
                (double) image.getHeight(null) / imageHeight);

        /* Scale to gradient format. */
        graphics.scale(imageWidth / (double) gradient.getWidth(null),
                imageHeight / (double) gradient.getHeight(null));
        graphics.drawImage(gradient, identity, null);

        /* Restore original transformation matrix. */
        graphics.setTransform(original);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        if(event.getWheelRotation() > 0) {
            chooserModel.setNextSelection(chooserModel.getNextSelection() + 1);
        } else {
            chooserModel.setNextSelection(chooserModel.getNextSelection() - 1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        double width = (double) getWidth();
        double imageWidth = width / (double) chooserModel.getNumVisibleImages();

        boolean found = false;

        int i = 1;
        while(i <= chooserModel.getNumVisibleImages() && !found) {
            if((double) event.getX() < (double) i * imageWidth) {
                /* Determine the index of the next selection. Rely on the fact
                 * that NUM_IMAGES is odd. */
                int index = chooserModel.getNextSelection() +
                        i - chooserModel.getNumVisibleImages() / 2 - 1;
                chooserModel.setNextSelection(index);
                model.setProvidedImage(chooserModel.getProvidedImage(index));
                found = true;
            }
            i++;
        }
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }
}
