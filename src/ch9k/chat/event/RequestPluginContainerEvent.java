package ch9k.chat.event;

import ch9k.chat.Conversation;

/**
 *
 * @author Jens Panneel
 */
public class RequestPluginContainerEvent extends ConversationEvent {
    private String title;

    public RequestPluginContainerEvent(
            Conversation conversation, String title) {
        super(conversation);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
