package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import java.awt.GridLayout;
import javax.swing.JPanel;

/**
 * The main Carousel panel.
 */
public class CarouselPanel extends JPanel {
    /**
     * The conversation.
     */
    private Conversation conversation;

    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The image panel.
     */
    private CarouselImagePanel imagePanel;

    /**
     * The image chooser panel.
     */
    private CarouselImageChooserPanel imageChooserPanel;

    /**
     * Constructor.
     * @param conversation The conversation of this plugin.
     * @param model The selection model of the plugin.
     */
    public CarouselPanel(Conversation conversation, CarouselImageModel model) {
        super(new GridLayout(2, 0));
        this.conversation = conversation;
        this.model = model;

        imagePanel = new CarouselImagePanel(model);
        add(imagePanel);

        imageChooserPanel = new CarouselImageChooserPanel(conversation, model);
        add(imageChooserPanel);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        imageChooserPanel.disablePlugin();
    }
}
