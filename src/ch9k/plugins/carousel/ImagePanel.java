package ch9k.plugins.carousel;

import ch9k.plugins.ProvidedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * View showing a single image. This class implements MouseListener for
 * convenience reasons, it doesn't actually do anything.
 */
public class ImagePanel extends JPanel
        implements MouseListener, ChangeListener {
    /**
     * If we should fit the image into the view.
     */
    private boolean fitImage;

    /**
     * If we should highlight the image when the mouse hovers over it.
     */
    private boolean highlight;

    /**
     * The actual image.
     */
    private ProvidedImage providedImage;

    /**
     * If the mouse is hovering over the component.
     */
    private boolean hover = false;

    /**
     * Color for the overlay.
     */
    private static final Color highlightColor = new Color(255, 255, 255, 75);

    /**
     * Optional background.
     */
    private Image background;

    /**
     * Constructor.
     * @param fitImage If we should fit the image into the view.
     * @param highlight If we should highlight the component on mouse hover.
     */
    public ImagePanel(boolean fitImage, boolean highlight) {
        this(fitImage, highlight, null);
    }

    /**
     * Constructor.
     * @param fitImage If we should fit the image into the view.
     * @param highlight If we should highlight the component on mouse hover.
     * @param background Optional background.
     */
    public ImagePanel(boolean fitImage, boolean highlight, Image background) {
        super();
        this.fitImage = fitImage;
        this.highlight = highlight;
        this.background = background;
        this.providedImage = null;
        setBackground(new Color(50, 50, 50));
    }

    /**
     * Set the image.
     * @param image Image to set.
     */
    public void setProvidedImage(ProvidedImage providedImage) {
        if(this.providedImage != null) {
            this.providedImage.removeChangeListener(this);
        }

        this.providedImage = providedImage;
        repaint();

        providedImage.addChangeListener(this);
    }

    /**
     * Get the provided image.
     * @return The provided image.
     */
    public ProvidedImage getProvidedImage() {
        return providedImage;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;

        /* Draw the background, if available. */
        if(background != null) {
            double x = width * 0.5 - background.getWidth(null) * 0.5;
            double y = height * 0.5 - background.getHeight(null) * 0.5;
            graphics.drawImage(background, insets.left + (int) x,
                    insets.top + (int) y, null);
        }

        /* If we have no image, we've now done enough. */
        if(providedImage == null || providedImage.getImage() == null) return;
        Image image = providedImage.getImage();

        /* Find out the ascpet ratio of the image. */
        double imageAspect =
                (double) image.getWidth(null) / image.getHeight(null);

        /* Scale by width. */
        double imageWidth = width;
        double imageHeight = (double) imageWidth / imageAspect;

        /* We're wrong, scale by imageHeight. */
        if(fitImage && imageHeight > height ||
                !fitImage && imageHeight < height) {
            imageHeight = height;
            imageWidth = imageAspect * (double) imageHeight;
        }

        /* Actually draw the image. */
        graphics.drawImage(image,
                insets.left + (int) (width * 0.5 - imageWidth * 0.5),
                insets.top + (int) (height * 0.5 - imageHeight * 0.5),
                (int) imageWidth, (int) imageHeight, Color.BLACK, null);

        /* If we have a highlight, draw a white overlay. */
        if(highlight && hover) {
            graphics.setColor(highlightColor);
            graphics.fillRect(
                insets.left + (int) (width * 0.5 - imageWidth * 0.5),
                insets.top + (int) (height * 0.5 - imageHeight * 0.5),
                (int) imageWidth, (int) imageHeight);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        hover = true;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent event) {
        hover = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        repaint();
    }
}
