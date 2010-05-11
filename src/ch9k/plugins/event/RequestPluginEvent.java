package ch9k.plugins.event;

import ch9k.chat.Conversation;
import ch9k.chat.event.ConversationEvent;

/**
 *
 * @author toon
 */
public class RequestPluginEvent extends ConversationEvent {

    private String pluginName;

    public RequestPluginEvent(Conversation conversation, String pluginName) {
        super(conversation);
        this.pluginName = pluginName;
    }

    public String getPlugin() {
        return pluginName;
    }
    
}
