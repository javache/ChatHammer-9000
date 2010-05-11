package ch9k.plugins.carousel;

import ch9k.core.I18n;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Action to preview an image.
 */
public class CarouselPreviewAction
        extends AbstractAction implements ChangeListener {
    /**
     * The model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     * @param model Model of which the image should be previewed.
     */
    public CarouselPreviewAction(CarouselImageModel model) {
        super(I18n.get("ch9k.plugins.carousel", "preview_image"));
        this.model = model;
        model.addChangeListener(this);
        setEnabled(false);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        setEnabled(model.getProvidedImage() != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProvidedImage image = model.getProvidedImage();
        CarouselImageFrame frame = new CarouselImageFrame(image);
        frame.setVisible(true);
    }
}
