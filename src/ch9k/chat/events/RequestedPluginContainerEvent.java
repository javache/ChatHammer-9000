package ch9k.chat.events;

import ch9k.chat.Conversation;
import java.awt.Container;

/**
 *
 * @author Jens Panneel
 */
public class RequestedPluginContainerEvent extends ConversationEvent {
    private Container pluginContainer;

    // should this event be broadcasted on network?
    public RequestedPluginContainerEvent(Conversation conversation,
            Container pluginContainer) {
        super(conversation);
        this.pluginContainer = pluginContainer;
    }

    public Container getPluginContainer() {
        return pluginContainer;
    }
}
