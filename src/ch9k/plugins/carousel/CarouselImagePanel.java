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
public class CarouselImagePanel extends ImagePanel implements ChangeListener {
    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     * @param model The selection model of the plugin.
     */
    public CarouselImagePanel(CarouselImageModel model) {
        super(true);
        setPreferredSize(new Dimension(400, 200));
        this.model = model;
        model.addChangeListener(this);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO: Be imageobserver
        /* Update the image. */
        if(model.getProvidedImage() != null) {
            setProvidedImage(model.getProvidedImage());
        }
    }
}
