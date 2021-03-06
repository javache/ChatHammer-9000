package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.ConversationSubject;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.NewConversationSubjectEvent;
import ch9k.core.settings.Settings;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventPool;
import ch9k.plugins.Plugin;
import ch9k.plugins.event.NewImageURLEvent;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class abstracting image providing.
 * @author Jasper Van der Jeugt
 */
public abstract class ImageProvider extends AbstractPluginInstance {
    /**
     * Constructor.
     * @param plugin The corresponding plugin.
     * @param conversation Conversation to provide images for.
     * @param settings Settings for the instance.
     */
    public ImageProvider(Plugin plugin,
            Conversation conversation, Settings settings) {
        super(plugin, conversation, settings);
    }
     
    @Override
    public void enablePluginInstance() {
        EventFilter filter = new ConversationEventFilter(
                NewConversationSubjectEvent.class, getConversation());
        EventPool.getAppPool().addListener(this, filter);
    }

    @Override
    public void disablePluginInstance() {
        EventPool.getAppPool().removeListener(this);
    }

    /**
     * Send new images, searching for text.
     * @param text Text to search for on the image provider.
     */
    public void sendNewImageEvent(String text) {
        /* Get the URL's from which the images should be loaded. */
        String[] urls = getImageUrls(text);

        /* When the concrete implementation fails, it will return null as urls.
         * However, a relevant warning will be shown by the concrete
         * implementation, so we can just return here. */
        if (urls == null) {
            return;
        }
        
        /* Load the actual images. */
        for (final String urlString: urls) {
            /* Create an actual url and throw an event. */
            try {
                URL url = new URL(urlString);
                NewImageURLEvent event =
                    new NewImageURLEvent(getConversation(), url);
                EventPool.getAppPool().raiseNetworkEvent(event);
            /* Exception found, ignore it. */
            } catch(MalformedURLException exception) {
            }
        }
    }

    @Override
    public void handleEvent(Event e) {
        super.handleEvent(e);
        if(e instanceof NewConversationSubjectEvent) {
            NewConversationSubjectEvent event = (NewConversationSubjectEvent) e;

            /* Only the one who initted the conversation should send images. */
            if(!getConversation().isInitiatedByMe()) {
                return;
            }

            /* Construct a new text to search for by appending subjects. */
            ConversationSubject subject = event.getConversationSubject();
            String[] subjects = subject.getSubjects();

            final StringBuilder text = new StringBuilder();
            if(subjects.length > 0) {
                text.append(subjects[0]);
            }
            for(int i = 1; i < subjects.length; i++) {
                text.append(" ").append(subjects[i]);
            }

            /* Send the new image event. */
            new Thread(new Runnable() {
                public void run() {
                    sendNewImageEvent(text.toString());
                }
            }).start();
        }
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @return List of URL's.
     */
    public String[] getImageUrls(String text) {
        return getImageUrls(text, 10);
    }

    /**
     * Get URL's of image results.
     * @param text Text to search for.
     * @param maxResults Maximum results to return.
     * @return List of URL's.
     */
    public abstract String[] getImageUrls(String text, int maxResults);
}
