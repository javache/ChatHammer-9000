package ch9k.plugins.carousel;

import ch9k.eventpool.WarningMessageEvent;
import ch9k.plugins.ProvidedImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * View showing a single image.
 */
public class CarouselImagePanel extends ImagePanel implements ChangeListener {
    /**
     * The selection model.
     */
    private CarouselImageModel model;

    private static final ImageIcon background = new ImageIcon(
            CarouselImagePanel.class.getResource(
                    "/ch9k/plugins/carousel/background.png"));

    /**
     * Constructor.
     * @param model The selection model of the plugin.
     */
    public CarouselImagePanel(CarouselImageModel model) {
        super(true, true, background.getImage());
        setPreferredSize(new Dimension(400, 200));
        this.model = model;
        model.addChangeListener(this);
        addMouseListener(this);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        setBackground(new Color(50, 50, 50));
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO: Be imageobserver
        /* Update the image. */
        if(model.getProvidedImage() != null) {
            setProvidedImage(model.getProvidedImage());
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        if(getProvidedImage() != null) {
            CarouselImageFrame frame =
                    new CarouselImageFrame(getProvidedImage());
            frame.setVisible(true);
        }
    }

    /**
     * Save the currently selected image to the hard disk.
     */
    private void save() {
        /* Show a dialog and let the user pick a file. */
        ProvidedImage provided = getProvidedImage();
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(provided.getFileName()));
        int result = chooser.showSaveDialog(this);

        /* Return unless we selected a file. */
        if(result != JFileChooser.APPROVE_OPTION) return;

        /* Create a new empty BufferedImage. */
        Image image = provided.getImage();
        BufferedImage buffer = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        
        /* Draw our image to this buffer. */
        Graphics2D graphics = buffer.createGraphics();
        graphics.drawImage(image, 0, 0, null);

        /* Save result using ImageIO class. */
        File selection = chooser.getSelectedFile();
        String path = selection.getPath();
        String extension = path.substring(path.lastIndexOf('.') + 1);
        try {
            ImageIO.write(buffer, extension, selection);
        } catch (IOException exception) {
            WarningMessageEvent.raiseWarningMessageEvent(this,
                "Could not save image " + selection);
        }
    }
}
