package ch9k.chat.event;

import ch9k.chat.Conversation;
import java.awt.Container;

/**
 *
 * @author Jens Panneel
 */
public class RequestedPluginContainerEvent extends ConversationEvent {
    private Container pluginContainer;

    public RequestedPluginContainerEvent(
            Conversation conversation, Container pluginContainer) {
        super(conversation);
        this.pluginContainer = pluginContainer;
    }

    public Container getPluginContainer() {
        return pluginContainer;
    }
}
