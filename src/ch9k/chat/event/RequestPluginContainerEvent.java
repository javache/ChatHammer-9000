package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class RequestPluginContainerEvent extends ConversationEvent {
    public RequestPluginContainerEvent(Conversation conversation) {
        super(conversation);
    }
}
