package ch9k.plugins.carousel;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.GroupLayout;

/**
 * Class to manage recommendations for the image carousel.
 */
public class CarouselRecommendationPanel extends JPanel {
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
     */
    public CarouselRecommendationPanel() {
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);

        recommendButton = new JButton("recommend");
        viewButton = new JButton("view");
        label = new JLabel("foobar");

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
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("foo");
        frame.setContentPane(new CarouselRecommendationPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
