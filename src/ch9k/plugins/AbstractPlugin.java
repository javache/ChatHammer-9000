package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import org.apache.log4j.Logger;

/**
 * Class implementing Plugin, with several utility functions added.
 * @author Jasper Van der Jeugt
 */
public abstract class AbstractPlugin implements Plugin {
    /**
     * The conversation we are bound to.
     */
    private Conversation conversation;

    /**
     * Logger logger logger
     * Mushroom Mushroom
     */
    private static final Logger logger = Logger.getLogger(AbstractPlugin.class);

    /**
     * Ask if a given ConversationEvent is relevant to the conversation this
     * Plugin is coupled with.
     * @param event The ConversationEvent.
     * @return If the event is relevant to this Plugin.
     */
    public boolean isRelevant(ConversationEvent event) {
        return conversation != null && event != null &&
                conversation == event.getConversation();
    }

    /**
     * Get the conversation this plugin is coupled with.
     * @return The coupled conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }

    @Override
    public void enable(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public void disable() {
        conversation = null;
    }
}
