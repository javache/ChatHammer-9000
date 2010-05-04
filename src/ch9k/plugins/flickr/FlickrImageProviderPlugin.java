package ch9k.plugins.flickr;

import ch9k.chat.Conversation;
import ch9k.core.settings.event.PreferencePaneEvent;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventPool;
import ch9k.plugins.AbstractPlugin;
import ch9k.plugins.AbstractPluginInstance;

/**
 * Plugin that gives us the ability to look up images on Flickr.
 * @author Jasper Van der Jeugt
 */
public class FlickrImageProviderPlugin extends AbstractPlugin {
    /**
     * Constructor.
     */
    public FlickrImageProviderPlugin () {
        /* Throw our preference pane. */
        Event event = new PreferencePaneEvent(getPrettyName(),
                new FlickrImageProviderPreferencePane(getSettings()));
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public AbstractPluginInstance createPluginInstance(
            Conversation conversation) {
        return new FlickrImageProvider(conversation);
    }
}
