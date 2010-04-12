package ch9k.plugins;

import ch9k.chat.Conversation;
import ch9k.chat.events.ConversationEvent;
import java.util.logging.Logger;

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
    private static final Logger LOGGER =
            Logger.getLogger(AbstractPlugin.class.getName());

    /**
     * Ask if a given ConversationEvent is relevant to the conversation this
     * Plugin is coupled with.
     * @param ConversationEvent The ConversationEvent.
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
    public void bind(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public void unbind(Conversation conversation) {
        if (this.conversation == conversation) {
            this.conversation = null;
        } else {
            LOGGER.warning("Trying to unbind a plugin from a conversation it" +
                    "wasn't bound to.");
        }
    }
}
