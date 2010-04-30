package ch9k.plugins.carousel;

import ch9k.core.I18n;
import ch9k.eventpool.EventPool;
import ch9k.plugins.ProvidedImage;
import ch9k.plugins.event.RecommendedImageEvent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Action to recommend an image.
 */
public class CarouselRecommendAction
        extends AbstractAction implements ChangeListener {
    /**
     * The model.
     */
    private CarouselImageModel model;

    /**
     * Constructor.
     * @param model Model of which the image should be recommended.
     */
    public CarouselRecommendAction(CarouselImageModel model) {
        super(I18n.get("ch9k.plugins.carousel", "recommend_image"));
        this.model = model;
        model.addChangeListener(this);
        setEnabled(false);
    }

    @Override
    public void stateChanged(ChangeEvent event) {
        setEnabled(model.getProvidedImage() != null &&
                !model.isRecommended());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProvidedImage image = model.getProvidedImage();
        
        if(image != null) {
            /* Raise the recommendation. */
            RecommendedImageEvent event = new RecommendedImageEvent(
                    model.getConversation(), image);
            EventPool.getAppPool().raiseEvent(event);

            /* Register our recommendation. */
            model.setRecommended(true);
        }
    }
}
