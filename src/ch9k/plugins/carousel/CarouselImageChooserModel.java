package ch9k.plugins.carousel;

import ch9k.chat.event.ConversationEventFilter;
import ch9k.core.Model;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.ProvidedImage;
import ch9k.plugins.event.NewProvidedImageEvent;
import java.util.HashSet;
import java.util.Set;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

/**
 * Class representing the image chooser data.
 */
public class CarouselImageChooserModel extends Model implements EventListener {
    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * Max number of images visible.
     */
    private static final int NUM_IMAGES = 10;

    /**
     * Array to store the images.
     */
    private ProvidedImage[] images;

    /**
     * We keep a set of images currently in the carousel, so we don't
     * accidentaly have two equal images.
     */
    private Set<ProvidedImage> imageSet;

    /**
     * Next selection.
     */
    int nextSelection;

    /**
     * Current selection.
     */
    double currentSelection;

    /**
     * Previous selection. We use a double because it can be halfway between
     * two selections.
     */
    double previousSelection;

    /**
     * Timer for animations.
     */
    private Timer timer;

    /**
     * Timer ticks passed.
     */
    private int ticks;

    /**
     * Constructor.
     */
    public CarouselImageChooserModel(CarouselImageModel model) {
        this.model = model;
        this.images = new ProvidedImage[NUM_IMAGES * 2];
        imageSet = new HashSet<ProvidedImage>();
        nextSelection = 0;
        currentSelection = 0.0;
        previousSelection = 0.0;

        /* Timer to update the animation. */
        timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ticks++;
                double diff = (double) currentSelection - previousSelection;
                currentSelection = previousSelection +
                        ((double) ticks * 0.1) * diff;
                if(ticks >= 10) {
                    currentSelection = (double) nextSelection;
                    timer.stop();
                }
                fireStateChanged();
            }
        });

        ticks = 0;

        /* Register as listener to receive new images. */
        EventFilter filter = new ConversationEventFilter(
                NewProvidedImageEvent.class, model.getConversation());
        EventPool.getAppPool().addListener(this, filter);
    }

    /**
     * Disable the plugin.
     */
    public void disablePlugin() {
        EventPool.getAppPool().removeListener(this);
    }

    /**
     * Obtain an image by index.
     * @param index Index of the image to obtain.
     * @return The requested image.
     */
    public ProvidedImage getProvidedImage(int index) {
        index += NUM_IMAGES;
        if(index < 0 || index >= NUM_IMAGES) {
            return null;
        } else {
            return images[index];
        }
    }

    /**
     * Obtain an image by index.
     * @param index Index of the image to obtain.
     * @return The requested image.
     */
    public Image getImage(int index) {
        ProvidedImage image = getProvidedImage(index);
        if(image == null) {
            return null;
        } else {
            return image.getImage();
        }
    }

    /**
     * Set the next selection.
     * @param nextSelection The next selection.
     */
    public void setNextSelection(int nextSelection) {
        if(this.nextSelection != nextSelection) {
            previousSelection = currentSelection;
            currentSelection = (double) this.nextSelection;
            this.nextSelection = nextSelection;

            /* Start timer for animation. */
            ticks = 0;
            timer.start();
        }
    }

    /**
     * Obtain the next selection.
     * @return The next selection index.
     */
    public int getNextSelection() {
        return nextSelection;
    }

    /**
     * Obtain the current selection.
     * @return The current selection index.
     */
    public double getCurrentSelection() {
        return currentSelection;
    }

    @Override
    public void handleEvent(Event e) {
        final NewProvidedImageEvent event = (NewProvidedImageEvent) e;

        /* Return if we have the image already. */
        ProvidedImage image = event.getProvidedImage();
        if(imageSet.contains(image)) return;

        /* We need to remove the old image from the set. */
        ProvidedImage old = images[0];
        if(old != null) imageSet.remove(old);

        /* Scroll the images. */
        for(int i = 0; i + 1 < images.length; i++) {
            images[i] = images[i + 1];
        }

        /* Set the new image. */
        images[images.length - 1] = image;
        imageSet.add(image);

        /* Update positions. */
        setNextSelection(nextSelection + 1);
    }
}
