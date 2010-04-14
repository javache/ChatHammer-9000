package ch9k.chat.events;

import ch9k.chat.Conversation;
import javax.swing.JPanel;

/**
 *
 * @author Jens Panneel
 */
public class RequestedPluginPanelEvent extends ConversationEvent {
    private JPanel pluginPanel;

    // should this event be broadcasted on network?
    public RequestedPluginPanelEvent(Conversation conversation, JPanel pluginPanel) {
        super(conversation);
        this.pluginPanel = pluginPanel;
    }

    public JPanel getPluginPanel() {
        return pluginPanel;
    }

}
