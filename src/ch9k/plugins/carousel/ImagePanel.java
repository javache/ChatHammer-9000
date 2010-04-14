package ch9k.plugins.carousel;

import ch9k.plugins.ProvidedImage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;

/**
 * View showing a single image. This class implements MouseListener for
 * convenience reasons, it doesn't actually do anything.
 */
public class ImagePanel extends JPanel implements MouseListener {
    /**
     * If we should fit the image into the view.
     */
    private boolean fitImage;

    /**
     * The actual image.
     */
    private ProvidedImage providedImage;

    /**
     * Constructor.
     * @param fitImage If we should fit the image into the view.
     */
    public ImagePanel(boolean fitImage) {
        super();
        this.fitImage = fitImage;
        this.providedImage = null;
    }

    /**
     * Set the image.
     * @param image Image to set.
     */
    public void setProvidedImage(ProvidedImage providedImage) {
        this.providedImage = providedImage;
        repaint();
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
        int width = getWidth();
        int height = getHeight();

        /* Paint background black. */
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

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
        graphics.drawImage(image, (int) (width * 0.5 - imageWidth * 0.5),
                (int) (height * 0.5 - imageHeight * 0.5), (int) imageWidth,
                (int) imageHeight, Color.BLACK, null);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
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
