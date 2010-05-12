package ch9k.plugins.carousel;

import java.net.URL;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.core.Model;
import ch9k.core.settings.Settings;
import ch9k.core.settings.SettingsChangeEvent;
import ch9k.core.settings.SettingsChangeListener;
import ch9k.core.settings.SettingsChangeListener;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.event.NewImageURLEvent;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Timer;
import java.util.List;
import java.util.ArrayList;

/**
 * Class representing the image chooser data.
 */
public class CarouselImageChooserModel extends Model
        implements EventListener, SettingsChangeListener {
    /**
     * Number of visible images on either side of the centered image.
     */
    private final static int NUM_SIDE_IMAGES = 2;

    /**
     * The selection model.
     */
    private CarouselImageModel model;

    /**
     * The plugin settings.
     */
    private Settings settings;

    /**
     * List to store the images.
     */
    private List<ProvidedImage> images;

    /**
     * We keep a set of URL's currently in the carousel, so we don't
     * accidentaly have two equal images.
     */
    private Set<URL> urls;

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
     * @param settins The plugin settings.
     * @param model The plugin selection model.
     */
    public CarouselImageChooserModel(Settings settings,
            CarouselImageModel model) {
        this.model = model;
        this.settings = settings;
        this.images = new ArrayList<ProvidedImage>();
        this.urls = new HashSet<URL>();
        nextSelection = NUM_SIDE_IMAGES;
        currentSelection = 0.0;
        previousSelection = 0.0;

        /* Timer to update the animation. */
        timer = new Timer(25, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                ticks++;
                double diff = (double) nextSelection - previousSelection;
                currentSelection = previousSelection +
                        (double) ticks * diff / 20.0;
                if(ticks >= 20) {
                    currentSelection = (double) nextSelection;
                    timer.stop();
                }
                fireStateChanged();
            }
        });

        ticks = 0;

        /* Register as listener to receive new images. */
        EventFilter filter = new ConversationEventFilter(
                NewImageURLEvent.class, model.getConversation());
        EventPool.getAppPool().addListener(this, filter);

        /* Listen to settings changes. */
        settings.addSettingsListener(this);
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
        if(index < 0 || index >= images.size()) {
            return null;
        } else {
            return images.get(index);
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
        if(nextSelection < NUM_SIDE_IMAGES) {
            nextSelection = NUM_SIDE_IMAGES;
        }

        if(nextSelection > images.size() - NUM_SIDE_IMAGES) {
            nextSelection = images.size() - NUM_SIDE_IMAGES - 1;
        }

        if(this.nextSelection != nextSelection) {
            previousSelection = currentSelection;
            this.nextSelection = nextSelection;

            /* Start timer for animation. */
            timer.stop();
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

    /**
     * Get the number of visible images on either side of the centered image.
     * @return Number of visible images.
     */
    public int getNumSideImages() {
        return NUM_SIDE_IMAGES;
    }

    /**
     * Get the total number of visible images.
     * @return The total number of visible images.
     */
    public int getNumVisibleImages() {
        return NUM_SIDE_IMAGES * 2 + 1;
    }

    /**
     * Add a new image. This might block for a while.
     * @param url URL to load.
     */
    private void addImage(URL url) {
        synchronized(this) {
            /* Return if we have the image already. */
            if(urls.contains(url)) return;

            /* Load the image. */
            ProvidedImage image = new ProvidedImage(url);

            /* Reject foobar images. */
            if(image.getImage() == null) return;

            /* If we already have enough images, we need to remove the old image
             * from the set. */
            if(images.size() >=
                    settings.getInt(CarouselPreferencePane.MAX_IMAGES)) {
                ProvidedImage old = images.get(0);
                urls.remove(old.getURL());
                images.remove(0);
            }

            /* Insert the new image. */
            images.add(image);
            urls.add(url);

            /* Update positions. */
            nextSelection--;
            currentSelection--;
            previousSelection = currentSelection;
            setNextSelection(nextSelection + 1);
            fireStateChanged();
        }
    }

    /**
     * Resize the images array.
     * @param size New size.
     */
    private void resizeImagesArray(int size) {
        synchronized(this) {
            List<ProvidedImage> old = images;
            images = new ArrayList<ProvidedImage>();
            urls.clear();
            for(int i = 0; i < size && i < old.size(); i++) {
                images.add(old.get(i));
                urls.add(old.get(i).getURL());
            }

            if(nextSelection >= size) {
                setNextSelection(size - 1);
            } else {
                fireStateChanged();
            }
        }
    }

    @Override
    public void handleEvent(Event e) {
        NewImageURLEvent event = (NewImageURLEvent) e;
        final URL url = event.getURL();
        new Thread(new Runnable() {
            public void run() {
                addImage(url);
            }
        }).start();
    }

    @Override
    public void settingsChanged(SettingsChangeEvent changeEvent) {
        resizeImagesArray(Integer.parseInt(changeEvent.getValue()));
    }
}
