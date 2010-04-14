package ch9k.chat.events;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class RequestPluginPanelEvent extends ConversationEvent {

    // should this event be broadcasted on network?
    public RequestPluginPanelEvent(Conversation conversation) {
        super(conversation);
    }
    
}
