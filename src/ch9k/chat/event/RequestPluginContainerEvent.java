package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class RequestPluginContainerEvent extends ConversationEvent {

    // should this event be broadcasted on network?
    public RequestPluginContainerEvent(Conversation conversation) {
        super(conversation);
    }
    
}
