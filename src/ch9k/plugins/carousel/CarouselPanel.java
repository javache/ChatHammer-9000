package ch9k.plugins.carousel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import ch9k.chat.Conversation;

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
