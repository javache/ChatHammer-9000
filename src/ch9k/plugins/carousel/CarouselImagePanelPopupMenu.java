package ch9k.plugins.carousel;

import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Class for a popup menu on the CarouselImagePanel.
 */
public class CarouselImagePanelPopupMenu extends JPopupMenu {
    /**
     * The model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     */
    public CarouselImagePanelPopupMenu(CarouselImageModel model) {
        super();
        this.model = model;

        add(new CarouselSaveImageAction(model));
    }
}
