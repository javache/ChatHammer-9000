package ch9k.plugins.carousel;

import ch9k.core.Model;
import ch9k.plugins.ProvidedImage;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Model representing the local recommendation queue.
 */
public class CarouselRecommendationQueue extends Model {
    /**
     * Internal queue.
     */
    private Queue<ProvidedImage> queue;

    /**
     * Constructor.
     */
    public CarouselRecommendationQueue() {
        queue = new LinkedList<ProvidedImage>();
    }

    /**
     * Push a ProvidedImage to the queue.
     * @param image Image to add to the queue.
     */
    public synchronized void push(ProvidedImage image) {
        if(image != null) {
            queue.offer(image);
            fireStateChanged();
        }
    }

    /**
     * Pop a ProvidedImage of the queue.
     * @return Next image, or none if empty.
     */
    public synchronized ProvidedImage pop() {
        if(queue.isEmpty()) {
            return null;
        } else {
            ProvidedImage image = queue.poll();
            fireStateChanged();
            return image;
        }
    }

    /**
     * Query the queue size.
     * @return The size of the queue.
     */
    public int size() {
        return queue.size();
    }
}
