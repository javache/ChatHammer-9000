package ch9k.plugins.carousel;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import ch9k.plugins.ProvidedImage;
import ch9k.plugins.RecommendedImageEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.SwingUtilities;
import java.util.Queue;
import java.util.LinkedList;
import ch9k.chat.events.ConversationEventFilter;
import ch9k.core.I18n;

/**
 * Class to manage recommendations for the image carousel.
 */
public class CarouselRecommendationPanel
        extends JPanel implements EventListener, ChangeListener {
    /**
     * The model.
     */
    private CarouselImageModel model;

    /**
     * Queue for recommended images.
     */
    private Queue<ProvidedImage> recommendations;

    /**
     * Button to recommend the current image.
     */
    private JButton recommendButton;

    /**
     * Button to view the recommended image.
     */
    private JButton viewButton;

    /**
     * Label containing some help text.
     */
    private JLabel label;

    /**
     * Constructor.
     * @param model Model for the selected image.
     */
    public CarouselRecommendationPanel(CarouselImageModel model) {
        this.model = model;
        recommendations = new LinkedList<ProvidedImage>();

        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);

        recommendButton = new JButton(
                I18n.get("ch9k.plugins.carousel", "recommend_image"));
        recommendButton.setEnabled(false);
        recommendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                recommend();
            }
        });

        viewButton = new JButton(
                I18n.get("ch9k.plugins.carousel", "view_recommendation"));
        viewButton.setEnabled(false);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                view();
            }
        });

        label = new JLabel();

        /* Horizontal group. */
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recommendButton)
                .addComponent(viewButton)
                .addComponent(label)
                .addContainerGap()
        );

        /* Vertical group. */
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                    .addComponent(recommendButton)
                    .addComponent(viewButton)
                    .addComponent(label))
            .addContainerGap());

        setLayout(layout);

        /* Start listening. */
        EventFilter filter = new ConversationEventFilter(
                RecommendedImageEvent.class, model.getConversation());
        EventPool.getAppPool().addListener(this, filter);
        model.addChangeListener(this);

        updateLabel();
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        EventPool.getAppPool().removeListener(this);
    }

    @Override
    public void handleEvent(Event e) {
        final RecommendedImageEvent event = (RecommendedImageEvent) e;

        /* Return when we threw this image ourselves. */
        // if(!event.isExternal()) return;

        /* Add the image to the queue. */
        recommendations.offer(event.getProvidedImage());

        /* Enable the "view image" button. */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                viewButton.setEnabled(true);
                updateLabel();
            }
        });
    }

    /**
     * Recommend the current image.
     */
    private void recommend() {
        ProvidedImage image = model.getProvidedImage();
        
        if(image != null) {
            /* Raise the recommendation. */
            RecommendedImageEvent event = new RecommendedImageEvent(
                    model.getConversation(), image);
            EventPool.getAppPool().raiseEvent(event);

            /* Disable the button, so we don't recommend it twice. */
            recommendButton.setEnabled(false);
        }
    }

    /**
     * View the recommended image.
     */
    private void view() {
        ProvidedImage image = recommendations.poll();
        if(image == null) return;

        model.setProvidedImage(image);

        if(recommendations.isEmpty()) {
            viewButton.setEnabled(false);
        }

        updateLabel();
    }

    /**
     * Update the text label.
     */
    private void updateLabel() {
        int size = recommendations.size();

        if(size == 0) {
            label.setText(
                I18n.get("ch9k.plugins.carousel", "no_recommendations"));
        } else if(size == 1) {
            label.setText(
                I18n.get("ch9k.plugins.carousel", "one_recommendation"));
        } else {
            label.setText(
                I18n.get("ch9k.plugins.carousel", "n_recommendations", size));
        }
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        // TODO: Be imageobserver
        /* Update the image. */
        if(model.getProvidedImage() != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    recommendButton.setEnabled(true);
                }
            });
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("foo");
        frame.setContentPane(new CarouselRecommendationPanel(null));
        frame.pack();
        frame.setVisible(true);
    }
}
