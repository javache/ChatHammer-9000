package ch9k.plugins.carousel;

import ch9k.core.I18n;
import ch9k.eventpool.WarningMessageEvent;
import ch9k.plugins.ProvidedImage;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Action to save an image.
 */
public class CarouselSaveImageAction
        extends AbstractAction implements ChangeListener {
    /**
     * The model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     * @param model Model of which the image should be saved.
     */
    public CarouselSaveImageAction(CarouselImageModel model) {
        super(I18n.get("ch9k.plugins.carousel", "save_image"));
        this.model = model;
        model.addChangeListener(this);
        setEnabled(false);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        setEnabled(model.getProvidedImage() != null);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /* Show a dialog and let the user pick a file. */
        ProvidedImage provided = model.getProvidedImage();
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(provided.getFileName()));
        int result = chooser.showSaveDialog(null);

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
