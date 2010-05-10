package be.jaspervdj.wordcloud;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEventFilter;
import ch9k.chat.event.ReleasePluginContainerEvent;
import ch9k.chat.event.RequestPluginContainerEvent;
import ch9k.chat.event.RequestedPluginContainerEvent;
import ch9k.chat.event.NewConversationSubjectEvent;
import ch9k.core.settings.Settings;
import ch9k.eventpool.Event;
import ch9k.eventpool.EventFilter;
import ch9k.eventpool.EventListener;
import ch9k.eventpool.EventPool;
import ch9k.plugins.AbstractPluginInstance;
import ch9k.plugins.Plugin;
import java.awt.Container;
import javax.swing.JLabel;

/**
 * WordCloud plugin view.
 */
public class WordCloud extends AbstractPluginInstance implements EventListener {
    /**
     * The container that we get from the system.
     */
    private Container container;

    /**
     * The word cloud panel.
     */
    private WordCloudPanel panel;

    /**
     * Constructor.
     * @param plugin Corresponding plugin.
     * @param conversation Conversation to display carousel for.
     * @param settings Local plugin instance settings.
     */
    public WordCloud(Plugin plugin,
            Conversation conversation, Settings settings) {
        super(plugin, conversation, settings);
        /* We will asynchronously receive a container later. */
        this.container = null;
    }

    @Override
    public void enablePluginInstance() {
        /* First, register this plugin as listener so it can receive a container
         * later. */
        EventFilter filter = new ConversationEventFilter(
                RequestedPluginContainerEvent.class, getConversation());
        EventPool.getAppPool().addListener(this, filter);

        /* Asyncrhonously request a panel for this plugin. */
        Event event = new RequestPluginContainerEvent(getConversation(),
                "WordCloud");
        EventPool.getAppPool().raiseEvent(event);
    }

    @Override
    public void disablePluginInstance() {
        /* Disable the plugin. */
        EventPool.getAppPool().removeListener(this);

        /* Stop the panel from listening. */
        if(panel != null) {
            EventPool.getAppPool().removeListener(panel);
        }

        /* Release the container request a panel for this plugin. */
        Event event =
                new ReleasePluginContainerEvent(getConversation(), container);
        EventPool.getAppPool().raiseEvent(event);
        container.removeAll();
        container = null;
    }

    @Override
    public void handleEvent(Event e) {
        RequestedPluginContainerEvent event = (RequestedPluginContainerEvent) e;

        /* We only need one panel. */
        if(container != null) return;

        /* Okay, we have a panel now, start using it. */
        container = event.getPluginContainer();

        /* Create a panel and make it listen. */
        panel = new WordCloudPanel();
        EventFilter filter = new ConversationEventFilter(
                NewConversationSubjectEvent.class, getConversation());
        EventPool.getAppPool().addListener(panel, filter);

        container.validate();
        container.repaint();
    }
}
