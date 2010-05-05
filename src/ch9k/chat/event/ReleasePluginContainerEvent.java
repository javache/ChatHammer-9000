package ch9k.chat.event;

import ch9k.chat.Conversation;
import java.awt.Container;

/**
 * Event thrown when a plugin releases a container.
 * @author Jasper Van der Jeugt
 */
public class ReleasePluginContainerEvent extends ConversationEvent {
    /**
     * Container to be released.
     */
    private transient Container pluginContainer;

    /**
     * Constructor.
     * @param conversation Relevant conversation.
     * @param pluginContainer Container to release.
     */
    public ReleasePluginContainerEvent(Conversation conversation,
            Container pluginContainer) {
        super(conversation);
        this.pluginContainer = pluginContainer;
    }

    /**
     * Get the container that should be released.
     * @return The container that should be released.
     */
    public Container getPluginContainer() {
        return pluginContainer;
    }
}
