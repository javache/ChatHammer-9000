package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class RequestPluginContainerEvent extends LocalConversationEvent {
    public RequestPluginContainerEvent(
            Object source, Conversation conversation) {
        super(source, conversation);
    }
}
