package ch9k.chat.event;

import ch9k.chat.Conversation;
import java.awt.Container;

/**
 *
 * @author Jens Panneel
 */
public class RequestedPluginContainerEvent extends LocalConversationEvent {
    private Container pluginContainer;

    public RequestedPluginContainerEvent(Object source,
            Conversation conversation, Container pluginContainer) {
        super(source, conversation);
        this.pluginContainer = pluginContainer;
    }

    public Container getPluginContainer() {
        return pluginContainer;
    }
}
