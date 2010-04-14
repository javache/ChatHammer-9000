package ch9k.plugins.carousel;

import ch9k.chat.Conversation;
import java.awt.BorderLayout;
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
     * The image chooser panel.
     */
    private CarouselImageChooserPanel imageChooserPanel;

    /**
     * Constructor.
     */
    public CarouselPanel(Conversation conversation) {
        super(new BorderLayout());
        this.conversation = conversation;

        imageChooserPanel = new CarouselImageChooserPanel(conversation);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        imageChooserPanel.disablePlugin();
    }
}
