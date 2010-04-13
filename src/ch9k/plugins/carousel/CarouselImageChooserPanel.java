package ch9k.plugins.carousel;

import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Panel in which the user can select an image.
 */
public class CarouselImageChooserPanel extends JPanel {
    /**
     * Constructor.
     */
    public CarouselImageChooserPanel() {
        super(new GridLayout(0, 6));
    }
}
