package ch9k.plugins;

import ch9k.chat.Conversation;

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
     * Get the conversation this plugin is coupled with.
     * @return The coupled conversation.
     */
    public Conversation getConversation() {
        return conversation;
    }

    @Override
    public void enablePlugin(Conversation conversation) {
        this.conversation = conversation;
    }

    @Override
    public void disablePlugin() {
        conversation = null;
    }
}
