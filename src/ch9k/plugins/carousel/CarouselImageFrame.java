package ch9k.plugins.carousel;

import ch9k.plugins.ProvidedImage;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;

public class CarouselImageFrame extends JFrame {
    /**
     * Constructor.
     * @param image Image to show in the frame.
     */
    public CarouselImageFrame(ProvidedImage image) {
        super();

        /* Set the content pane. */
        ImagePanel imagePanel = new ImagePanel(true, false);
        imagePanel.setProvidedImage(image);
        setContentPane(imagePanel);

        /* Set the title. */
        setTitle(new File(image.getURL().getFile()).getName());

        /* Try to get the window size right. */
        setPreferredSize(new Dimension(image.getImage().getWidth(null),
                image.getImage().getHeight(null)));

        /* Pack the window. */
        pack();
    }
}
