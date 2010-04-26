package ch9k.plugins.carousel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
}
